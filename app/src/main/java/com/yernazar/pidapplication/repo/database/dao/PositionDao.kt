package com.yernazar.pidapplication.repo.database.dao

import androidx.room.*
import org.jguniverse.pidapplicationgm.repo.model.Position
import org.jguniverse.pidapplicationgm.repo.model.Stop

@Dao
interface PositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(position: Position)

    @Update
    fun update(position: Position)

    @Delete
    fun delete(position: Position)

    @Query("delete from Position")
    fun deleteAllPositions()
}