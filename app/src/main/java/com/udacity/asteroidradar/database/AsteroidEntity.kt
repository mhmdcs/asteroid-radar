package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

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
    val isPotentiallyHazardous: Boolean)

@Entity(tableName = "picture_of_day_table")
data class PictureOfDayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long=0L,
    val mediaType: String,
    val title: String,
    val url: String)
