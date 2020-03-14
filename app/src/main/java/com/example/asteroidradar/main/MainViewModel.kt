package com.example.asteroidradar.main

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.asteroidradar.R
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.RetrofitFactory.getRetrofitFactory
import com.udacity.asteroidradar.api.getTodaysDate
import com.udacity.asteroidradar.api.isNetworkAvailable
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _photoOfTheDay = MutableLiveData<PictureOfDay>()
    val photoOfTheDay: LiveData<PictureOfDay> = _photoOfTheDay

    private val _asteroidListLiveData: MutableLiveData<List<Asteroid>> = MutableLiveData()
    val asteroidListLiveData: LiveData<List<Asteroid>> = _asteroidListLiveData

    private val database: AsteroidDatabase = AsteroidDatabase.getDatabase(application)
    private val repository = AsteroidRepository(database.asteroidDao())

    init {
        if (isNetworkAvailable(application)) {
            viewModelScope.launch {
                val response = getRetrofitFactory().getPictureOfTheDay()
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.mediaType == "image") {
                            _photoOfTheDay.value = it
                        }
                    }

                    repository.fetchFromNetworkIfNeeded(getTodaysDate())
                }
            }
        } else {
            Toast.makeText(
                application, application.getString(
                    R.string.no_internet
                ), Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun loadWeeksAsteroidList(): LiveData<List<Asteroid>> {
        return repository.getWeeksAsteroidList(getTodaysDate())
    }

    fun loadTodayAsteroidList(): LiveData<List<Asteroid>> {
        return repository.getTodayAsteroidList(getTodaysDate())
    }

    fun loadAllAsteroidList(): LiveData<List<Asteroid>> {
        return repository.getAllSavedAsteroidList()
    }

    fun loadAsteroidList(asteroidList: List<Asteroid>) {
        _asteroidListLiveData.value = asteroidList
    }
}