package com.kesimilim.newsmap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kesimilim.newsmap.model.CopyHistory
import com.kesimilim.newsmap.model.Post

data class RoomPostCoryHistory(
    @Embedded
    val post: Post,
    @Relation(
        parentColumn = "post_owner_id",
        entity = CopyHistory::class,
        entityColumn = "cory_history_owner_id"
    )
    val attachment: List<CopyHistory>
)
