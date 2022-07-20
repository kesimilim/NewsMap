package com.kesimilim.newsmap.repository.friendDataSource.remote

import com.kesimilim.newsmap.database.entity.RoomFriend

interface FriendRemoteDataSource {
    suspend fun getFriendList(): List<RoomFriend>
}