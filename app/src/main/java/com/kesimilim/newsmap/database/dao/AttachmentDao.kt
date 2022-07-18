package com.kesimilim.newsmap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kesimilim.newsmap.database.entity.RoomAttachment

@Dao
interface AttachmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addAttachment(attachment: RoomAttachment)

    @Query("SELECT * FROM attachment_table WHERE attachment_post_id = :postId")
    fun getAttachmentById(postId: Int): List<RoomAttachment>
}