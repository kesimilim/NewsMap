package com.kesimilim.newsmap.database

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var INSTANCE: NewsMapDatabase? = null

    fun getInstance(context: Context): NewsMapDatabase {
        if (INSTANCE == null) {
            synchronized(NewsMapDatabase::class.java) {
                INSTANCE = buildDatabase(context)
            }
        }
        return INSTANCE!!
    }

    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            NewsMapDatabase::class.java,
            "NewsAppDatabase"
        ).build()
}