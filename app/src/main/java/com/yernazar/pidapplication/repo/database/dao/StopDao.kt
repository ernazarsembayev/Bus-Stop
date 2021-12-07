package com.yernazar.pidapplication.repo.database.dao

import androidx.room.*
import org.jguniverse.pidapplicationgm.repo.model.Stop
import org.jguniverse.pidapplicationgm.repo.model.Trip

@Dao
interface StopDao {

    @Query("SELECT * FROM stop")
    fun getAll(): List<Stop>?

    @Query("SELECT * FROM stop WHERE uid LIKE :stopId")
    fun getById(stopId: String): Stop?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stop: List<Stop>)

    @Update
    fun update(stop: Stop)

    @Delete
    fun delete(stop: Stop)

    @Query("delete from Stop")
    fun deleteAllStops()

//    @Query("select * from note_table order by priority desc")
//    fun getAllNotes(): LiveData<List<Note>>

}