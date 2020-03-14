package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidsDao {

    @Query("Select * from Asteroid where closeApproachDate >= :fromDate ORDER by closeApproachDate")
    fun getWeeksAsteroidList(fromDate: String): LiveData<List<Asteroid>>

    @Query("Select * from Asteroid where closeApproachDate = :date")
    fun getTodayAsteroidList(date: String): LiveData<List<Asteroid>>

    @Query("Select * from Asteroid ORDER by closeApproachDate")
    fun getAllSavedAsteroidList(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroidList: List<Asteroid>)

    @Query("Select * from Asteroid")
    suspend fun getAllAsteroidList(): List<Asteroid>
}
