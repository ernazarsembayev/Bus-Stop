package com.yernazar.pidapplication.repo.database.dao

import androidx.room.*
import org.jguniverse.pidapplicationgm.repo.model.Shape
import org.jguniverse.pidapplicationgm.repo.model.Stop

@Dao
interface ShapeDao {

    @Insert
    suspend fun insert(shape: List<Shape>)

    @Update
    suspend fun update(shape: Shape)

    @Delete
    suspend fun delete(shape: Shape)

    @Query("delete from Shape")
    suspend fun deleteAllShapes()
}