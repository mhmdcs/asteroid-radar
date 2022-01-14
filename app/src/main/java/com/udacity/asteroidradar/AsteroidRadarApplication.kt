package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import com.udacity.asteroidradar.work.RefreshAsteroidDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

//the Application class is the main object the operating system uses to interact with our app
//you can think of this as "the thing" that lives under every activity and screen in the app
class AsteroidRadarApplication: Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     * You can test that by calling Thread.sleep(4_000) in onCreate, which will cause the app to freeze for 4 seconds before the first screen is displayed
     * Use it to setup any background tasks, and don't forget to run any expensive setup operations in a background thread to avoid delaying app start.
     */

    //application onCreate is a really important method, it runs every time every time the app is launched,
    //and it has to run before any screen is shown, to check this, you can Thread.sleep(4_000)
    //and you'll notice a huge delay before screens show because the app is stuck in onCreate for a short while
    override fun onCreate() {
        super.onCreate()
        // Thread.sleep(4_000) //don't forget to comment out delayedInit() when you run Thread.sleep
       // Timber.plant(Timber.DebugTree()) annoying ass library yelling at u with lint warnings ugh
        delayedInit()
    }

    //instead of doing initialization in onCreate before the first screen shows, we do it outside of onCreate in a separate method to make the code look neat, and run it in the background.
    //we can use a coroutine by calling "launch" to run it in the background thread.
    private fun delayedInit() {
        applicationScope.launch {
            //now that the initialization is done out of onCreate in a separate thread, we're ready to tell WorkManager to run our job
            setupRecurringWork()
        }
    }

    //this function sets up recurring work that handles scheduling the work
    private fun setupRecurringWork() {
        //set constraints so that the work doesn't drain users' Android phone batteries
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()


        //WorkManager schedules work with a work request, there are two types of work request, a one-time type, and a periodic type
        //since we want to have our sync task work running daily, it'll be periodic type request
        //in the constructor we tell it how often to schedule this work, and then we ask it to run every day, then call build
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshAsteroidDataWorker>(1,TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        //to schedule work, we use enqueue method on WorkManager
        //enqueueUniquePeriodicWork is work that's scheduled to execute regularly or on a periodic basis
        //it's also unique which means we tell WorkManager what the unique work name is for this work
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshAsteroidDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    } //this is a very battery-friendly daily pre-fetch scheduling, with this, users will get the freshest data every time they open the app

}