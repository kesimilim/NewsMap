package com.kesimilim.newsmap.database.dao.relation

import androidx.room.Dao
import androidx.room.Query
import com.kesimilim.newsmap.model.Attachment

@Dao
interface CopyHistoryAttachment {
    @Query("SELECT * FROM attachment_table WHERE attachment_post_id = :postId")
    fun getCopyHistoryAttachment(postId: String): List<Attachment>
}