package com.kesimilim.newsmap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kesimilim.newsmap.model.Attachment
import com.kesimilim.newsmap.model.CopyHistory

data class RoomCopyHistoryAttachment(
    @Embedded
    val copyHistory: CopyHistory,
    @Relation(
        parentColumn = "cory_history_owner_id",
        entity = Attachment::class,
        entityColumn = "attachment_owner_id"
    )
    val attachment: List<Attachment>
)
