package com.kesimilim.newsmap.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kesimilim.newsmap.model.Attachment
import com.kesimilim.newsmap.model.Post
import com.kesimilim.newsmap.model.CopyHistory

@Entity(tableName = "post_table")
data class RoomPost(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = 0,

    @ColumnInfo(name = "post_id")
    override val postId: Int? = null,

    @ColumnInfo(name = "post_owner_id")
    override val ownerId: Long?,

    @ColumnInfo(name = "post_text")
    override val postText: String? = null,
//    @ColumnInfo(name = "post_attachment")
//    override val attachment: List<Attachment>? = null,
//    @ColumnInfo(name = "post_copy_history")
//    override val copyHistory: List<CopyHistory>? = null,
): Post
