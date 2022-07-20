package com.kesimilim.newsmap.database.entity

import androidx.room.*
import com.vk.dto.common.id.UserId

@Entity(tableName = "friends_table")
data class RoomFriend(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "photo")
    val photo: String,

    @Embedded
    val city: RoomCity,

    @Embedded
    val vkUserId: UserId
) {
    data class RoomCity(
        val name: String?,
        val latitude: Double?,
        val longitude: Double?
    )
}
