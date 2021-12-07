package com.yernazar.pidapplication.repo.database.dao

import androidx.room.*
import org.jguniverse.pidapplicationgm.repo.model.Shape
import org.jguniverse.pidapplicationgm.repo.model.Stop

@Dao
interface ShapeDao {

    @Query("SELECT * FROM Shape WHERE uid = :id ORDER BY ptSequence")
    fun getById(id: String): List<Shape>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(shape: List<Shape>)

    @Update
    fun update(shape: Shape)

    @Delete
    fun delete(shape: Shape)

    @Query("delete from Shape")
    fun deleteAllShapes()
}