package com.kesimilim.newsmap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kesimilim.newsmap.database.dao.FriendsDao
import com.kesimilim.newsmap.database.entity.FriendsRoom

@Database(entities = [FriendsRoom::class],  exportSchema = true, version = 1)
abstract class NewsMapDatabase: RoomDatabase() {
    abstract fun FriendsDao(): FriendsDao

}