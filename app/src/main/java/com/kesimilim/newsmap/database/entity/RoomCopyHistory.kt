package com.kesimilim.newsmap.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesimilim.newsmap.model.CopyHistory

@Entity(tableName = "post_cory_history_table")
data class RoomCopyHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,

    @ColumnInfo(name = "copy_history_post_id")
    override val postId: Int?,

    @ColumnInfo(name = "cory_history_owner_id")
    override val ownerId: Long?,
): CopyHistory
