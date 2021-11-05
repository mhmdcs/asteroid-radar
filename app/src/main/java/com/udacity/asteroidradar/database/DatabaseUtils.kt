package com.udacity.asteroidradar.database

import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay


//from Database to Domain models

//utility helpful extension function/convenience method
//to convert Asteroid data from database(entity) model to domain model using transformations map
//will help you in the repository class
fun List<AsteroidEntity>.asDomainModel(): List<Asteroid>{
    return map {
        //asteroidEntity ->
        Asteroid(
            //id = asteroidEntity.id
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

//to convert PictureOfDay data from database(entity) model to domain model using transformations map
//will help you in the repository class
fun PictureOfDayEntity.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}