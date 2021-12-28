package com.yernazar.pidapplication.data.repository.database.dao

import androidx.room.*
import com.yernazar.pidapplication.data.repository.model.Position

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