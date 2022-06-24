package com.kesimilim.newsmap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kesimilim.newsmap.database.entity.FriendsRoom

@Dao
interface FriendsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFriend(friend: FriendsRoom)

    @Query("SELECT * FROM friends_table")
    fun getAllFriends(): List<FriendsRoom>
}