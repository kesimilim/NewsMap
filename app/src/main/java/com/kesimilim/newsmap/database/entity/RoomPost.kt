package com.kesimilim.newsmap.database.entity

import androidx.room.*
import com.kesimilim.newsmap.model.Attachment
import com.kesimilim.newsmap.model.Post
import com.kesimilim.newsmap.model.CopyHistory

@Entity(
    tableName = "post_table",
    foreignKeys = [
        ForeignKey(
            entity = RoomFriend::class,
            parentColumns = arrayOf("user_id"),
            childColumns = arrayOf("post_user_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["post_user_id"],
            unique = true
        ),
        Index(
            value = ["post_id"],
            unique = true
        )
    ]
)
data class RoomPost(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "post_id")
    override val postId: Int? = null,

    @ColumnInfo(name = "post_user_id")
    override val userId: Long? = null,

    @ColumnInfo(name = "post_text")
    override val postText: String? = null,

    @ColumnInfo(name = "post_attachment")
    val attachment: Boolean = false,

    @ColumnInfo(name = "post_copy_history")
    val copyHistory: Boolean = false,
): Post
