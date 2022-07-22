package com.kesimilim.newsmap.repository.friendDataSource.local

import com.kesimilim.newsmap.database.dao.FriendsDao
import com.kesimilim.newsmap.database.entity.RoomFriend

class FriendLocalDataSourceImpl(private val friendDao: FriendsDao): FriendLocalDataSource {

    override suspend fun getFriendList(): List<RoomFriend> = friendDao.getAllFriends()

    override suspend fun setFriendList(friendList: List<RoomFriend>) {
        friendList.map { friend ->
            friendDao.addFriend(friend)
        }
    }

    override suspend fun getFriend(id: Long): RoomFriend = friendDao.getFriend(id)
}