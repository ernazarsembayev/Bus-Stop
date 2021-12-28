package com.yernazar.pidapplication.data.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yernazar.pidapplication.data.repository.database.dao.*
import com.yernazar.pidapplication.data.repository.model.*

@Database(entities = [Trip::class, Route::class, Stop::class, ShapeOld::class, Position::class, Vehicle::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao
    abstract fun routeDao(): RouteDao
    abstract fun stopDao(): StopDao
    abstract fun shapeDao(): ShapeDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun positionDao(): PositionDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): AppDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java,
                    "note_database")
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!

        }
    }
}