package com.yernazar.pidapplication.repo.database.dao

import androidx.room.*
import org.jguniverse.pidapplicationgm.repo.model.Stop
import org.jguniverse.pidapplicationgm.repo.model.Vehicle

@Dao
interface VehicleDao {

    @Query("SELECT * FROM Vehicle WHERE tripId LIKE :tripId")
    fun getByTripId(tripId: String): Vehicle?

    @Query("SELECT * FROM Vehicle WHERE nextStopId LIKE :stopId")
    fun getByNextStopId(stopId: String): Vehicle?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vehicle: Vehicle)

    @Update
    fun update(vehicle: Vehicle)

    @Delete
    fun delete(vehicle: Vehicle)

    @Query("delete from Vehicle")
    fun deleteAllVehicles()

}