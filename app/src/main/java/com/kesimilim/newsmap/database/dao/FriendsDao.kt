package com.kesimilim.newsmap.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kesimilim.newsmap.database.entity.RoomFriend

@Dao
interface FriendsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFriend(friend: RoomFriend)

    @Query("SELECT * FROM friends_table WHERE user_id = :id")
    fun getFriend(id: Long): RoomFriend

    @Query("SELECT * FROM friends_table")
    fun getAllFriends(): List<RoomFriend>
}