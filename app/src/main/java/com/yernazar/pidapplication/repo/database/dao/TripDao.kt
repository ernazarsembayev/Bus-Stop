package com.yernazar.pidapplication.repo.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.jguniverse.pidapplicationgm.repo.model.Stop
import org.jguniverse.pidapplicationgm.repo.model.Trip

@Dao
interface TripDao {

    @Query("SELECT * FROM trip WHERE routeId = :routeId")
    suspend fun getByRouteId(routeId: String): Flow<Trip>

    @Insert
    suspend fun insert(trip: Trip)

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("delete from Trip")
    suspend fun deleteAllTrips()
}