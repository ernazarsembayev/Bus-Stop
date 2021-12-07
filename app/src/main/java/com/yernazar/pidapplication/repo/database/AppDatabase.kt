package com.yernazar.pidapplication.repo.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yernazar.pidapplication.repo.database.dao.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jguniverse.pidapplicationgm.repo.model.*
import java.sql.Timestamp
import java.time.LocalDateTime

@Database(entities = [Trip::class, Route::class, Stop::class, Shape::class, Position::class, Vehicle::class], version = 1)
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
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: AppDatabase) {

            // Bus stop "Václavské náměstí"
            // У остановки с названием "Václavské náměstí" есть 2 физические остановки (в одну сторону и другую)

            val stop1 = Stop("1","Václavské náměstí",
                50.08167, 14.42528, "zone_id",
                1, "parent location")

            val stop2 = Stop("U484Z2P","Václavské náměstí",
                50.08196, 14.42572, "zone_id",
                1, "parent location")

            val stop3 = Stop("3","Poslední autobusová zastávka",
                50.08167, 14.42528, "zone_id",
                1, "parent location")

            val stop4 = Stop("4","Poslední autobusová zastávka",
                50.08196, 14.42572, "zone_id",
                1, "parent location")

            val trip = Trip("9_6952_211202", "L9",
                "1111100-1", "L9V2", 0,
                0, "Spojovací", true,
                bikesAllowed = false,  "")

            // это одна точка из множества в json
            val shape = Shape("L9V2", 50.0652, 14.29887, 0.0, 1)

            val route = Route("L9", "Sídliště Řepy - Spojovací",
                "9", "", "99", "7A0603",
                "FFFFFF", "0", "https://pid.cz/linka/9", isNight = false)

            val position = Position(12,"9_6952_211202", 50.07914, 14.42059,
                1, 10.4, "U484Z2P", "U997Z2P", false)

            val vehicle = Vehicle(12, "9_6952_211202", "9",
                "", 0, 50.07914, 14.42059,
                0, 10.4, true, 1,
                1, 1, false, "3",
                System.currentTimeMillis() - 6000,  "U484Z2P",
                System.currentTimeMillis(), VehicleType.TRAM, 0)

            Log.e("populate", "database")
            val stopDao = db.stopDao()
            val tripDao = db.tripDao()
            val shapeDao = db.shapeDao()
            val routeDao = db.routeDao()
            val vehicleDao = db.vehicleDao()
            val positionDao = db.positionDao()

            GlobalScope.launch {
                stopDao.insert(listOf(stop1, stop2, stop3, stop4))
                tripDao.insert(trip)
                shapeDao.insert(listOf(shape))
                routeDao.insert(route)
                vehicleDao.insert(vehicle)
                positionDao.insert(position)
            }
        }
    }

}