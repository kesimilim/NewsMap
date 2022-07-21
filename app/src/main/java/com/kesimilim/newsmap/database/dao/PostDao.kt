package com.kesimilim.newsmap.database.dao

import androidx.room.*
import com.kesimilim.newsmap.database.entity.RoomPost
import com.kesimilim.newsmap.database.entity.RoomPostWithAttachment

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPost(post: RoomPost)

    @Query("SELECT * FROM post_table WHERE post_user_id = :userId")
    fun getPostById(userId: Long): List<RoomPost>

    @Transaction
    @Query("SELECT * FROM post_table WHERE post_user_id = :userId")
    fun getPostAttachmentList(userId: Long): List<RoomPostWithAttachment>
}