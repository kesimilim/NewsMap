package com.kesimilim.newsmap.database.entity

import androidx.room.*
import com.kesimilim.newsmap.model.Attachment
import com.kesimilim.newsmap.model.CopyHistory
import com.kesimilim.newsmap.model.Post

@Entity(
    tableName = "attachment_table",
    foreignKeys = [
        ForeignKey(
            entity = Post::class,
            parentColumns = arrayOf("post_id"),
            childColumns = arrayOf("attachment_post_id"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CopyHistory::class,
            parentColumns = arrayOf("copy_history_post_id"),
            childColumns = arrayOf("attachment_post_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["attachment_post_id"],
            unique = true
        )
    ]
)
data class RoomAttachment(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,

    @ColumnInfo(name = "attachment_post_id")
    val postId: Int? = null,

    override val photo: String? = null
): Attachment