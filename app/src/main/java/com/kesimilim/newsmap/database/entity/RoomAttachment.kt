package com.kesimilim.newsmap.database.entity

import androidx.room.*
import com.kesimilim.newsmap.model.Attachment

@Entity(
    tableName = "attachment_table",
//    foreignKeys = [
//        ForeignKey(
//            entity = RoomPost::class,
//            parentColumns = ["post_id"],
//            childColumns = ["attachment_post_id"],
//            onDelete = ForeignKey.CASCADE
//        ),
////        ForeignKey(
////            entity = RoomCopyHistory::class,
////            parentColumns = ["copy_history_post_id"],
////            childColumns = ["attachment_post_id"],
////            onDelete = ForeignKey.CASCADE
////        )
//    ],
//    indices = [
//        Index(
//            value = ["attachment_post_id"],
//            unique = true
//        )
//    ]
)
data class RoomAttachment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "attachment_post_id")
    val postId: Int?,

    override val photo: String? = null
): Attachment