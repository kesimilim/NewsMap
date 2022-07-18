package com.kesimilim.newsmap.database.dao.relation

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kesimilim.newsmap.database.entity.RoomAttachment

@Dao
interface PostAttachmentDao {
    @Transaction
    @Query("SELECT * FROM attachment_table WHERE attachment_post_id = :postId")
    fun getPostAttachment(postId: Int): List<RoomAttachment>
}