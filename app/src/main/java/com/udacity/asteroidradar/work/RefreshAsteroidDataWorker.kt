package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRadarRepository
import retrofit2.HttpException

//Definition 1: WorkManager is a Jetpack library to manage work while your app may be in the background, or not even running.
//Definition 2: WorkManager is an API that makes it easy to schedule reliable asynchronous tasks
//that are expected to run even if the app exits or the device restarts.

//You specify work by extending the Worker class and implementing one method doWork().
//But even better, instead of extending Worker, you should extend CoroutineWorker, which has a suspending version of doWork()

//Compared to Coroutines or other ways to do work on Android, you don't tell WorkManager *how* to do work, but
//rather you tell it under *what constraints* to do the work, constraints are things like "when the phone is charging"
//or "when the phone has network" or "when user is not using the device".

//You tell WorkManager to run work once, or periodically, for example, tell it to run work everyday.
//WorkManager will take these constraints and figure out how and when to best run the work.
//For example you might tell it your work should run when the device is charging and when it's on Wi-Fi,
//and WorkManager will make sure your work runs only when both are true.

//Constraints are really important, without constraints, background work is one of the largest drainers of battery on Android phones.
//To avoid battery draining for users, optimize your code and make sure your work runs rarely, by scheduling the longest possible day for repeating tasks.
//and to check if you need to do background work at all to begin, the most friendly background work is the kind that never happens at all lol.

//Here we will use WorkManagers to fetch Asteroids and PictureOfDay in the background once a day, even if the user isn't using the app, this is called pre-fetching
//and it's a great way to make sure users get fresh content immediately upon opening the app.
//and by specifying constraints, WorkManager will figure out how to do this in the background with the least impact on the users' batteries.


class RefreshAsteroidDataWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshAsteroidDataWorker"
    }


    //our worker will run until doWork returns the Result, so we can do anything we want here,
    //like accessing the network, writing to the database, etc. and WorkManager will make sure that the operating system
    //won't interrupt our work.

    //doWork() will run in the background thread, so we don't have to worry about blocking the UI thread.
    override suspend fun doWork(): Result {
        //inside doWork, we get the database and the repository, just like we do in the ViewModel
        val database = getDatabase(applicationContext)
        val asteroidRepo = AsteroidRadarRepository(database)

        return try {
            asteroidRepo.refreshPictureOfDay()
            asteroidRepo.refreshAsteroids()

            Result.success() //to let WorkManager know we're done, we return Result.success()
        }
        //but what if a network request fails? this can happen even if the device has network,
        //the backend server might be offline for example, we don't want to tell WorkManager we had a success if network request failed

        catch (exc: HttpException) {
            Result.retry() //so if the network request fails and there's an HttpException from retrofit, we ask the WorkManager to retry the job at later time with Result.retry()
        }
    }


} //now that we're done defining our background work with this class, we schedule it in an Application class