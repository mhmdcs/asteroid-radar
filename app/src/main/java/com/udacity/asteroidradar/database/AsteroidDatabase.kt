package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class], version = 1)
abstract class AsteroidDatabase: RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    //a more optimized implementation of database initialization
    //to prevent multiple instances being created, initialize the database in a singleton (companion object)
    //if you don't synchronise here, then two different threads could both create a new instance of the database
    // whereas the singleton pattern is supposed to facilitate a single shared instance across the lifecycle of the program
    companion object {

        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): AsteroidDatabase {
            val localInstance = INSTANCE

            if (localInstance != null) {
                return localInstance
            }

            return synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroids"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                instance
            }
        }
    }

}


    //this implementation of database initialization below is not thread-safe
    //it is recommended for the database initialization to be a singleton (companion object) due to expensive initialisation cost with synchronization
//    private lateinit var INSTANCE: AsteroidDatabase
//
//    fun getDatabase(context: Context): AsteroidDatabase {
//        synchronized(AsteroidDatabase::class.java){
//            if(!::INSTANCE.isInitialized){//if instance of the database is null, then create it
//            INSTANCE = Room
//                .databaseBuilder(context.applicationContext, AsteroidDatabase::class.java,"asteroids")
//                .build()
//            }
//        }
//        return INSTANCE
//    }