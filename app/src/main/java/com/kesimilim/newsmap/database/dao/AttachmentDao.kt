package com.kesimilim.newsmap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.kesimilim.newsmap.database.entity.RoomAttachment

@Dao
interface AttachmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAttachment(attachment: RoomAttachment)
}