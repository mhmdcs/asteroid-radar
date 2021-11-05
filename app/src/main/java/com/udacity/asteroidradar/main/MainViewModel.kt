package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import com.udacity.asteroidradar.utils.SortFilter
import kotlinx.coroutines.launch

//extending AndroidViewModel gives you the application context
class MainViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepo = AsteroidRadarRepository(database)

    private val _navigateToDetailsFragment = MutableLiveData<Asteroid>()
    val navigateToDetailsFragment: LiveData<Asteroid>
    get() = _navigateToDetailsFragment

    init {
        viewModelScope.launch {
            asteroidRepo.refreshAsteroids()
            asteroidRepo.refreshPictureOfDay()
        }
    }

    val pictureOfDay = asteroidRepo.pictureOfDay
    val asteroidList = asteroidRepo.asteroids

    fun onChangeSortFilter(sortFilter: SortFilter){
        asteroidRepo.changeSortFilter(sortFilter)
    }

    fun asteroidClicked(asteroid: Asteroid){
        _navigateToDetailsFragment.value = asteroid
    }

    fun doneNavigation(){
        _navigateToDetailsFragment.value = null
    }

}