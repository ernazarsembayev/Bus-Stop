package com.yernazar.pidapplication.repo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yernazar.pidapplication.repo.database.dao.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jguniverse.pidapplicationgm.repo.model.*
import java.sql.Timestamp

@Database(entities = [Trip::class, Route::class, Stop::class, Shape::class, Position::class], version = 1)
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

            val stop2 = Stop("1","Václavské náměstí",
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

            val position = Position("9_6952_211202", 50.07914, 14.42059,
                1, 10.4, "U484Z2P", "U997Z2P", false)

            val vehicle = Vehicle("9_6952_211202", "9",
                "", 0, 50.07914, 14.42059,
                0, 10.4, true, 1,
                1, 1, false, "U997Z2P",
                Timestamp(0),  "U484Z2P",
                Timestamp(0), VehicleType.TRAM, 0)

            val stopDao = db.stopDao()
            val tripDao = db.tripDao()
            val shapeDao = db.shapeDao()
            val routeDao = db.routeDao()
            val vehicleDao = db.vehicleDao()
            val positionDao = db.positionDao()

            GlobalScope.launch {
                stopDao.insert(listOf(stop1, stop2))
                tripDao.insert(trip)
                shapeDao.insert(listOf(shape))
                routeDao.insert(route)
                vehicleDao.insert(vehicle)
                positionDao.insert(position)
            }
        }
    }

}