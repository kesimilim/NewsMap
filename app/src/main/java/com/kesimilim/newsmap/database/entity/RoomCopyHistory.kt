package com.kesimilim.newsmap.database.entity

import androidx.room.*

@Entity(tableName = "cory_history_table")
data class RoomCopyHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "copy_history_post_id")
    val postId: Int?,

    @ColumnInfo(name = "post_text")
    val postText: String? = null,

    @ColumnInfo(name = "post_attachment")
    val attachment: Boolean = false,
)
