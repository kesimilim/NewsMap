package com.kesimilim.newsmap.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RoomPostWithAttachment (
    @Embedded
    val post: RoomPost,
    @Relation(
        parentColumn = "post_id",
        entity = RoomAttachment::class,
        entityColumn = "attachment_post_id"
    )
    val attachmentList: List<RoomAttachment>
)