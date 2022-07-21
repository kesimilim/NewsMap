package com.kesimilim.newsmap.database.entity

import androidx.room.*

@Entity(tableName = "post_table")
data class RoomPost(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "post_user_id")
    val userId: Long? = null,

    @ColumnInfo(name = "post_id")
    val postId: Int?,

    @ColumnInfo(name = "post_text")
    val postText: String? = null,

    @ColumnInfo(name = "copy_history_text")
    val copyHistoryText: String? = null,

    @ColumnInfo(name = "post_attachment")
    val attachment: Boolean = false,
)
