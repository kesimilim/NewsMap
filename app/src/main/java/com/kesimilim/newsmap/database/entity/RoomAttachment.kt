package com.kesimilim.newsmap.database.entity

import androidx.room.*
import com.vk.sdk.api.photos.dto.PhotosPhotoSizes

@Entity(tableName = "attachment_table")
data class RoomAttachment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "attachment_post_id")
    val postId: Int?,

    @Embedded
    val photo: PhotosPhotoSizes? = null
)