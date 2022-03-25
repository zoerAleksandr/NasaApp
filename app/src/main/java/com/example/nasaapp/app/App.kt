package com.example.nasaapp.app

import android.app.Application
import androidx.room.Room
import com.example.nasaapp.model.room.PodDAO
import com.example.nasaapp.model.room.PodDatabase

class App: Application() {

    companion object {
        private var appInstance: App? = null
        private var db: PodDatabase? = null
        private const val DB_NAME = "History.db"
        fun getPodDao(): PodDAO { if (db == null) {
            synchronized(PodDatabase::class.java) { if (db == null) {
                if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                db = Room.databaseBuilder( appInstance!!.applicationContext, PodDatabase::class.java, DB_NAME) .allowMainThreadQueries() .build()
            } }
        }
            return db!!.podDao() }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}