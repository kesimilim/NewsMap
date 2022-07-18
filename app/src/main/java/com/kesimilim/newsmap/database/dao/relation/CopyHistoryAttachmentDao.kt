package com.kesimilim.newsmap.database.dao.relation

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kesimilim.newsmap.database.entity.RoomAttachment

@Dao
interface CopyHistoryAttachmentDao {
    @Transaction
    @Query("SELECT * FROM attachment_table WHERE attachment_post_id = :postId")
    fun getCopyHistoryAttachment(postId: Int): List<RoomAttachment>
}