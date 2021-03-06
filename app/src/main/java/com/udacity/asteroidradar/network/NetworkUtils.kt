package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.utils.Constants.DEFAULT_END_DATE_DAYS
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//DataTransferObjects are responsible for parsing responses from the server
//or formatting objects to send to the server. Convert these network objects to domain objects before using them.

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()
    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
            asteroidList.add(asteroid)
        }
    }

    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()
    for (i in 0..7) { //instead of 7 you can use Constants.DEFAULT_END_DATE_DAYS which refers to 7 of type int in the Constants class
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

//will help in the repository class
fun getTodayDateFormatted(): String {
    val calendar =  Calendar.getInstance()
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    return dateFormat.format(currentTime)
}

//will help in the repository class
fun getNextWeekDateFormatted(): String {
    val calendar =  Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS) //DEFAULT_END_DATE_DAYS is just 7 of type int
    val currentTime = calendar.time
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    return dateFormat.format(currentTime)
}

//from Network to Domain models

//utility helpful extension function/convenience method, shorter implementation than the commented out code below
//to convert (Network)PictureOfDay data from network model to domain model using transformations map
fun NetworkPictureOfDay.asDomainModel() : PictureOfDay {
    return PictureOfDay(mediaType, title, url)
}

//fun NetworkPictureOfDay.toDomainModel(): PictureOfDay {
//    return PictureOfDay(
//        mediaType = this.mediaType,
//        title = this.title,
//        url = this.url
//    )
//}