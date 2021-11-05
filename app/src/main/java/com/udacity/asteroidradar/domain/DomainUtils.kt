package com.udacity.asteroidradar.domain

import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.PictureOfDayEntity

//from Domain to Database models

//utility helpful extension function/convenience method
//to convert Asteroid data from domain model to database(entity) model using transformations map
//will help you in the repository class
fun ArrayList<Asteroid>.asDatabaseModel():Array<AsteroidEntity> {
    return map { asteroid ->
        AsteroidEntity(
            id = asteroid.id,
            codename = asteroid.codename,
            closeApproachDate = asteroid.closeApproachDate,
            absoluteMagnitude = asteroid.absoluteMagnitude,
            estimatedDiameter = asteroid.estimatedDiameter,
            relativeVelocity = asteroid.relativeVelocity,
            distanceFromEarth = asteroid.distanceFromEarth,
            isPotentiallyHazardous = asteroid.isPotentiallyHazardous
        )
    }.toTypedArray()
}


//to convert PictureOfDay data from domain model to database(entity) model using transformations map
//will help you in the repository class
fun PictureOfDay.asDatabaseModel(): PictureOfDayEntity {
    return PictureOfDayEntity(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}