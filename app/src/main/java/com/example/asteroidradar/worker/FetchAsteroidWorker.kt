package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.RetrofitFactory.getRetrofitFactory
import com.udacity.asteroidradar.api.getTodaysDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import org.json.JSONObject

class FetchAsteroidWorker(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        return try {
            val asteroidsResponse = getRetrofitFactory().getAsteroids(getTodaysDate())
            if (asteroidsResponse.isSuccessful) {
                val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidsResponse.body()!!)).toList()
                AsteroidDatabase.getDatabase(context.applicationContext).asteroidDao().insertAll(asteroidList)
                return Result.success()
            } else {
                Result.failure()
            }
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}

