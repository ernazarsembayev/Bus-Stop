package com.yernazar.pidapplication.repo.database.dao

import androidx.room.*
import org.jguniverse.pidapplicationgm.repo.model.Stop

@Dao
interface StopDao {

    @Insert
    suspend fun insert(stop: List<Stop>)

    @Update
    suspend fun update(stop: Stop)

    @Delete
    suspend fun delete(stop: Stop)

    @Query("delete from Stop")
    suspend fun deleteAllStops()

//    @Query("select * from note_table order by priority desc")
//    fun getAllNotes(): LiveData<List<Note>>

}