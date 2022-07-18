package com.kesimilim.newsmap.database.entity

import androidx.room.*
import com.kesimilim.newsmap.model.CopyHistory

@Entity(
    tableName = "cory_history_table",
    foreignKeys = [
        ForeignKey(
            entity = RoomPost::class,
            parentColumns = ["post_id"],
            childColumns = ["copy_history_post_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(
            value = ["copy_history_post_id"],
            unique = true
        )
    ]
)
data class RoomCopyHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "copy_history_post_id")
    override val postId: Int?,

    @ColumnInfo(name = "post_text")
    val postText: String? = null,

    @ColumnInfo(name = "post_attachment")
    val attachment: Boolean = false,
): CopyHistory
