package com.kesimilim.newsmap.database.entity

import android.content.Context
import android.location.Geocoder
import androidx.room.*
import com.kesimilim.newsmap.model.City
import com.kesimilim.newsmap.model.Friend
import java.io.Serializable

@Entity(
    tableName = "friends_table",
    indices = [
        Index(
            value = arrayOf("user_id"),
            unique = true
        )
    ]
)
data class RoomFriend(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "user_id")
    override val userId: Long = 0,

    @ColumnInfo(name = "first_name")
    override val firstName: String,

    @ColumnInfo(name = "last_name")
    override val lastName: String,

    @ColumnInfo(name = "photo")
    override val photo: String,

    @Embedded
    override val city: RoomCity,
) : Friend, Serializable {

    data class RoomCity(
//        @PrimaryKey(autoGenerate = true)
//        val id: Int? = 0,

//        @ColumnInfo(name = "name")
        override val name: String?,

//        @ColumnInfo(name = "latitude")
        override var latitude: Double?,

//        @ColumnInfo(name = "longitude")
        override var longitude: Double?
    ): City
}
