package com.kesimilim.newsmap.database.entity

import android.content.Context
import android.location.Geocoder
import androidx.room.*
import com.kesimilim.newsmap.model.City
import com.kesimilim.newsmap.model.Friend
import com.vk.dto.common.id.UserId
import java.io.Serializable

@Entity(
    tableName = "friends_table",
//    indices = [
//        Index(
//            value = arrayOf("user_id"),
//            unique = true
//        )
//    ]
)
data class RoomFriend(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name = "first_name")
    override val firstName: String,

    @ColumnInfo(name = "last_name")
    override val lastName: String,

    @ColumnInfo(name = "photo")
    override val photo: String,

    @Embedded
    override val city: RoomCity,

    @Embedded
    val vkUserId: UserId
) : Friend, Serializable {

    data class RoomCity(
        override val name: String?,
        override var latitude: Double?,
        override var longitude: Double?
    ): City
}
