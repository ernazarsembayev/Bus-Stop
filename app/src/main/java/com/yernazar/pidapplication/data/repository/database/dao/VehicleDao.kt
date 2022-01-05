package com.yernazar.pidapplication.data.repository.database.dao

import androidx.room.*

//@Dao
//interface VehicleDao {
//
//    @Query("SELECT * FROM Vehicle WHERE tripId LIKE :tripId")
//    fun getByTripId(tripId: String): Vehicle?
//
//    @Query("SELECT * FROM Vehicle WHERE nextStopId LIKE :stopId")
//    fun getByNextStopId(stopId: String): Vehicle?
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insert(vehicle: Vehicle)
//
//    @Update
//    fun update(vehicle: Vehicle)
//
//    @Delete
//    fun delete(vehicle: Vehicle)
//
//    @Query("delete from Vehicle")
//    fun deleteAllVehicles()
//
//}