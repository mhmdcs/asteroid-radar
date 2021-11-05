package com.udacity.asteroidradar.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.database.AsteroidDao
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

class AsteroidRadarRepository(private val dao: AsteroidDao) {

    @RequiresApi(Build.VERSION_CODES.O)
    private val _startDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val _endDate = _startDate.plusDays(7)

    private val _sortFilter = MutableLiveData(SortFilter.TODAY)

    fun changeSortFilter(sortFilter: SortFilter){
        _sortFilter.value = sortFilter
    }

    //transformation via switchMap https://stackoverflow.com/questions/47610676/how-and-where-to-use-transformations-switchmap
    @RequiresApi(Build.VERSION_CODES.O)
    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(_sortFilter){
    when(it)  {

        SortFilter.WEEKLY ->
            Transformations.map(dao.getThisWeekAsteroids(_startDate.toString(), _endDate.toString()))
            {asteroidEntity ->
                asteroidEntity.asDomainModel()
            }

        SortFilter.TODAY ->
            Transformations.map(dao.getThisDayAsteroids(_startDate.toString()))
            {asteroidEntity ->
                asteroidEntity.asDomainModel()
            }

        SortFilter.CACHED ->
            Transformations.map(dao.getCachedAsteroids())
            {asteroidEntity ->
                asteroidEntity.asDomainModel()
            }

        else -> throw IllegalArgumentException()
    }
    }

    val pictureOfDay: LiveData<PictureOfDay> =
        Transformations.map(dao.getPictureOfDay()) { pictureEntity ->
            pictureEntity?.asDomainModel()
        }

    //get the new Asteroids
    suspend fun refreshAsteroids(){
    withContext(Dispatchers.IO){
        try {
            val startDate = getTodayDateFormatted()
            val endDate = getNextWeekDateFormatted()
            val asteroidsResult = NasaApi.retrofitService.getAsteroids(startDate, endDate)
            val parsedAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsResult))
            dao.insertAll(*parsedAsteroids.asDatabaseModel())
        } catch (error: Exception){
            error.printStackTrace()
        }
    }
    }

    //get the new Picture Of The Day
    suspend fun refreshPictureOfDay(){
    withContext(Dispatchers.IO){
        try {
            val picture = NasaApi.retrofitService.getPictureOfDay()
            val domainPicture = picture.asDomainModel()
            if(domainPicture.mediaType=="image"){ //or try "picture"?
                dao.clearPicture()
                dao.insertAllPictures()
            }
        }catch(error: Exception){
            error.printStackTrace()
        }
    }
    }


}