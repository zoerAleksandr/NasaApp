package com.example.nasaapp.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [PodEntity::class], version = 1, exportSchema = false)
abstract class PodDatabase: RoomDatabase() {
    abstract fun podDao(): PodDAO
}