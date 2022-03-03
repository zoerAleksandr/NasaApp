package com.example.nasaapp.model.room

import androidx.room.*

@Dao
interface PodDAO {
    @Query("SELECT * FROM PodEntity")
    fun all(): List<PodEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: PodEntity)

    @Delete
    fun delete(entity: PodEntity)
}