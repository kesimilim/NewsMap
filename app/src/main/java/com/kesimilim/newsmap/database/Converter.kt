package com.kesimilim.newsmap.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kesimilim.newsmap.model.FriendPost

class Converter {
    @TypeConverter
    fun fromFriendPost(value: List<FriendPost>): String {
        val gson = Gson()
        val type = object : TypeToken<List<FriendPost>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toFriendPost(value: String): List<FriendPost> {
        val gson = Gson()
        val type = object : TypeToken<List<FriendPost>>() {}.type
        return gson.fromJson(value, type)
    }
}