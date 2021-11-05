package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import retrofit2.HttpException

class RefreshAsteroidDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshAsteroidsDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val asteroidRepo = AsteroidRadarRepository(database)

        return try {
            asteroidRepo.refreshPictureOfDay()
            asteroidRepo.refreshAsteroids()

            Result.success()
        } catch (exc: HttpException) {
            Result.retry()
        }
    }
}