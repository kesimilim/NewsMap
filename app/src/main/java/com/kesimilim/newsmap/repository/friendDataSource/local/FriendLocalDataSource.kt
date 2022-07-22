package com.kesimilim.newsmap.repository.friendDataSource.local

import com.kesimilim.newsmap.database.entity.RoomFriend

interface FriendLocalDataSource {
    suspend fun getFriendList(): List<RoomFriend>
    suspend fun setFriendList(friendList: List<RoomFriend>)
    suspend fun getFriend(id: Long): RoomFriend
}