package com.yernazar.pidapplication.data.repository.database.dao

import androidx.room.*
import com.yernazar.pidapplication.data.repository.model.ShapeOld

@Dao
interface ShapeDao {

    @Query("SELECT * FROM ShapeOld WHERE uid = :id ORDER BY ptSequence")
    fun getById(id: String): List<ShapeOld>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shapeOld: List<ShapeOld>)

    @Update
    fun update(shapeOld: ShapeOld)

    @Delete
    fun delete(shapeOld: ShapeOld)

    @Query("delete from ShapeOld")
    fun deleteAllShapes()
}