package com.udacity.asteroidradar.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.asDatabaseModel
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.utils.SortFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception
import java.time.LocalDate

class AsteroidRadarRepository(private val database: AsteroidDatabase) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _startDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _endDate = _startDate.plusDays(7)

    private val _sortFilter = MutableLiveData(SortFilter.WEEKLY)

    private val sortFilter : LiveData<SortFilter>
        get() = _sortFilter

    fun changeSortFilter(sortFilter: SortFilter){
        _sortFilter.value = sortFilter
    }

    //transformation via switchMap https://stackoverflow.com/questions/47610676/how-and-where-to-use-transformations-switchmap
    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids : LiveData<List<Asteroid>> = Transformations.switchMap(sortFilter)
    {
        when(it){
            SortFilter.WEEKLY ->
                Transformations.map(database.asteroidDao.getThisWeekAsteroids(_startDate.toString(), _endDate.toString())) { asteroidEntities ->
                    asteroidEntities.asDomainModel()
                }

            SortFilter.TODAY ->
                Transformations.map(database.asteroidDao.getThisDayAsteroids(_startDate.toString())) {asteroidEntities ->
                    asteroidEntities.asDomainModel()
                }

            SortFilter.CACHED ->
                Transformations.map(database.asteroidDao.getCachedAsteroids()) { asteroidEntities ->
                    asteroidEntities.asDomainModel()
                }

            else -> throw IllegalArgumentException("")
        }
    }

    //get the new Asteroids
    suspend fun refreshAsteroids(){
    withContext(Dispatchers.IO){
        try {
            val startDate = getTodayDateFormatted()
            val endDate = getNextWeekDateFormatted()
            val asteroidsResult = NasaApi.retrofitService.getAsteroids(startDate, endDate)
            Log.i("LogRepoTest1",asteroidsResult)
            Log.i("LogRepoTest2",startDate)
            Log.i("LogRepoTest3",endDate)
            val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResult))
            database.asteroidDao.insertAll(*parsedAsteroids.asDatabaseModel())
        } catch (error: Exception){
            error.printStackTrace()
        }
    }
    }

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(database.asteroidDao.getPictureOfDay()) { pictureEntity ->
            pictureEntity?.asDomainModel()
        }

    //get the new Picture Of The Day
    suspend fun refreshPictureOfDay(){
        withContext(Dispatchers.IO){
            try {
                val networkPicture = NasaApi.retrofitService.getPictureOfDay()
                Log.i("LogRepoTest4",networkPicture.toString())
                val domainPicture = networkPicture.asDomainModel()
                if(domainPicture.mediaType=="image"){ //or try "picture"?
                    database.asteroidDao.clearPicture()
                    database.asteroidDao.insertAllPictures(domainPicture.asDatabaseModel())
                }
            }catch(error: Exception){
                error.printStackTrace()
            }
        }
    }


}