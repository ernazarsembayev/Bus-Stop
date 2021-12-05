package com.yernazar.pidapplication.repo.database.dao

import androidx.room.*
import org.jguniverse.pidapplicationgm.repo.model.Position
import org.jguniverse.pidapplicationgm.repo.model.Stop

@Dao
interface PositionDao {

    @Insert
    suspend fun insert(position: Position)

    @Update
    suspend fun update(position: Position)

    @Delete
    suspend fun delete(position: Position)

    @Query("delete from Position")
    suspend fun deleteAllPositions()
}