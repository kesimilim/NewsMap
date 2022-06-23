package com.kesimilim.newsmap.database

import androidx.room.TypeConverter
import com.kesimilim.newsmap.database.entity.FriendsRoom

class TypeConverter {

    @TypeConverter
    fun fromFriends(value: FriendsRoom): List<FriendsRoom> {
        val list = mutableListOf<FriendsRoom>()
        list.add(value)
        return list
    }

//    @TypeConverter
//    fun fromList(list: List<FriendsRoom>) {
//        return list.joinToString(",")
//    }

}