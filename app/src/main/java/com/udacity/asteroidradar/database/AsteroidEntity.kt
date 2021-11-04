package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid

//Entities are database objects

@Entity(tableName = "asteroids_table")
data class AsteroidEntity(

    //columns
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean){

    //utility helpful extension function/convenience method to convert
    //from database model to domain model using transformations map
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

}

