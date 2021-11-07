package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.utils.Constants.API_KEY
import com.udacity.asteroidradar.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()


interface NasaApiService {

    @GET("/neo/rest/v1/feed") //HTTP request method GET to retrieve data. Value is an API endpoint URI
    suspend fun getAsteroids( //retrofit service methods must be suspended
        @Query("start_date") startDate: String, //will look like https://api.nasa.gov/neo/rest/v1/feed?start_date=YY-MM-DD&api_key=YOUR_API_KEY
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY //will look like https://api.nasa.gov/neo/rest/v1/feed?api_key=YOUR_API_KEY
    ): String

    @GET("/planetary/apod") //HTTP request method GET to retrieve data. Value is an API endpoint URI
    suspend fun getPictureOfDay( //retrofit service methods must be suspended
        @Query("api_key") apiKey: String = API_KEY //will look like https://api.nasa.gov/planetary/apod?api_key=YOUR_API_KEY
    ): NetworkPictureOfDay

}

object NasaApi {

    //lazy Initialization is a performance optimization
    //where you postpone potentially expensive object creation until just before you actually need it
    //like initializing/creating a retrofit service

    val retrofitService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }

    //val retrofitService = retrofit.create(NasaApiService::class.java)

}