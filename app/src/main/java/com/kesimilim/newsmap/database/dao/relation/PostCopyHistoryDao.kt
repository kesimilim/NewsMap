package com.kesimilim.newsmap.database.dao.relation

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kesimilim.newsmap.database.entity.RoomCopyHistory

@Dao
interface PostCopyHistoryDao {
    @Transaction
    @Query("SELECT * FROM cory_history_table WHERE copy_history_post_id = :postId")
    fun getPostCopyHistory(postId: Int): List<RoomCopyHistory>
}