package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidDatabase: RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

//    companion object {
//        private lateinit var INSTANCE: AsteroidDatabase
//
//        fun getDatabase(context: Context): AsteroidDatabase {
//            synchronized(this){
//                if(!::INSTANCE.isInitialized){//if instance of the database is null, then create it
//                    INSTANCE = Room
//                        .databaseBuilder(context.applicationContext, AsteroidDatabase::class.java,"asteroids")
//                        .build()
//                }
//            }
//            return INSTANCE
//        }
//    }

}

    private lateinit var INSTANCE: AsteroidDatabase

    fun getDatabase(context: Context): AsteroidDatabase {
        synchronized(AsteroidDatabase::class.java){
            if(!::INSTANCE.isInitialized){//if instance of the database is null, then create it
            INSTANCE = Room
                .databaseBuilder(context.applicationContext, AsteroidDatabase::class.java,"asteroids")
                .build()
            }
        }
        return INSTANCE
    }