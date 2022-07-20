package com.kesimilim.newsmap.repository

import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.repository.friendDataSource.local.FriendLocalDataSource
import com.kesimilim.newsmap.repository.friendDataSource.remote.FriendRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FriendRepository(
    private val friendRemoteDataSource: FriendRemoteDataSource,
    private val friendLocalDataSource: FriendLocalDataSource
) {
    suspend fun fetchFriendList(): List<RoomFriend> = withContext(Dispatchers.IO) {
        friendLocalDataSource.getFriendList().ifEmpty {
            val friendList = friendRemoteDataSource.getFriendList()
            friendLocalDataSource.setFriendList(friendList)
            friendList
        }
    }
}