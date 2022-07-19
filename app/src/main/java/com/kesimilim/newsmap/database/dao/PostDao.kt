package com.kesimilim.newsmap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kesimilim.newsmap.database.entity.RoomPost

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPost(post: RoomPost)

    @Query("SELECT * FROM post_table WHERE post_user_id = :userId")
    fun getPostById(userId: Long): List<RoomPost>
}