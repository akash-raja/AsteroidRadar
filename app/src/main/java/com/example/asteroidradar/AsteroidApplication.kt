package com.example.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.Constants.WORK_TAG
import com.udacity.asteroidradar.worker.FetchAsteroidWorker
import java.util.concurrent.TimeUnit

class AsteroidApplication() : Application() {

    override fun onCreate() {
        super.onCreate()

        val workManager = WorkManager.getInstance()
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).setRequiresCharging(true).build()

        val request =
            PeriodicWorkRequest.Builder(FetchAsteroidWorker::class.java, 24, TimeUnit.HOURS).setConstraints(constraints)
                .build()

        workManager.enqueueUniquePeriodicWork(WORK_TAG, ExistingPeriodicWorkPolicy.KEEP, request)
    }
}