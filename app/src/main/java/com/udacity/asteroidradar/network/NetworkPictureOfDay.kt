package com.udacity.asteroidradar.network

import com.squareup.moshi.Json

//@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)