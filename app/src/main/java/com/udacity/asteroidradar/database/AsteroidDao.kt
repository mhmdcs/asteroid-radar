package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    //Dao (Data Access Objects) are query CRUD operations

    //Asteroids Dao
    @Insert(onConflict = OnConflictStrategy.REPLACE) //Upsert CRUD (Insert-Update) that inserts a row(record) into a column if it doesn't exist, and updates it if it does exist
    suspend fun insertAll(vararg asteroid: AsteroidEntity) //suspend functions execute a long running operation and wait for it to complete without blocking the main thread

    @Query("select * from asteroids_table order by date(closeApproachDate) asc") //ascending: from bottom to top
    fun getCachedAsteroids(): LiveData<List<AsteroidEntity>>

    @Query("select * from asteroids_table where closeApproachDate = :date")
    fun getThisDayAsteroids(date: String): LiveData<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate BETWEEN :startDate AND :endDate")
    fun getThisWeekAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>


    //Pictures Dao
    @Query("select * from picture_of_day_table")
    fun getPictureOfDay() : LiveData<PictureOfDayEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPictures(vararg pictureEntity: PictureOfDayEntity)

    @Query("delete from picture_of_day_table")
    fun  clearPicture()

}