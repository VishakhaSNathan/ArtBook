package com.example.artbook.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ArtDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(art: Art)

    @Delete
    suspend fun delete(art: Art)

    @Query("select * from arts")
    fun observeArts() : LiveData<List<Art>>
}