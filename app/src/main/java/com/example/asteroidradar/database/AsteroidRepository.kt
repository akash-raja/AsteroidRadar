package com.example.asteroidradar.database

import androidx.lifecycle.LiveData
import com.example.asteroidradar.Asteroid
import com.example.asteroidradar.api.RetrofitFactory.getRetrofitFactory
import com.example.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject

class AsteroidRepository(private val asteroidsDao: AsteroidsDao) {

    fun getWeeksAsteroidList(fromDate: String): LiveData<List<Asteroid>> {
        return asteroidsDao.getWeeksAsteroidList(fromDate)
    }

    suspend fun fetchFromNetworkIfNeeded(fromDate: String) {
        if (asteroidsDao.getAllAsteroidList().isNullOrEmpty()) {
            val asteroidsResponse = getRetrofitFactory().getAsteroids(fromDate)
            if (asteroidsResponse.isSuccessful) {
                val parseAsteroidsJsonResult = parseAsteroidsJsonResult(JSONObject(asteroidsResponse.body()!!)).toList()
                insertAsteroidListToDatabase(parseAsteroidsJsonResult)
            }
        }
    }

    fun getTodayAsteroidList(date: String): LiveData<List<Asteroid>> {
        return asteroidsDao.getTodayAsteroidList(date)
    }

    fun getAllSavedAsteroidList(): LiveData<List<Asteroid>> {
        return asteroidsDao.getAllSavedAsteroidList()
    }

    private suspend fun insertAsteroidListToDatabase(asteroidList: List<Asteroid>) {
        asteroidsDao.insertAll(asteroidList)
    }
}