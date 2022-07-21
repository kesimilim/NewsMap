package com.kesimilim.newsmap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kesimilim.newsmap.database.dao.AttachmentDao
import com.kesimilim.newsmap.database.dao.FriendsDao
import com.kesimilim.newsmap.database.dao.PostDao
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.database.entity.RoomPost

@Database(
    entities = [
        RoomAttachment::class,
        RoomFriend::class,
        RoomPost::class,
    ], version = 1
)
abstract class NewsMapDatabase: RoomDatabase() {
    abstract fun friendsDao(): FriendsDao
    abstract fun postDao(): PostDao
    abstract fun attachmentDao(): AttachmentDao
}