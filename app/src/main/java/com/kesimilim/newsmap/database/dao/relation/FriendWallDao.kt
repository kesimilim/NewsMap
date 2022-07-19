package com.kesimilim.newsmap.database.dao.relation

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kesimilim.newsmap.database.entity.RoomPost

@Dao
interface FriendWallDao {
    @Transaction
    @Query("SELECT * FROM post_table WHERE post_user_id = :userId")
    fun getFriendWallDao(userId: Long): List<RoomPost>
}