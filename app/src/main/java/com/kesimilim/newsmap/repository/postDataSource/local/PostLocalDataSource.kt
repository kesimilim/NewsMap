package com.kesimilim.newsmap.repository.postDataSource.local

import com.kesimilim.newsmap.database.entity.RoomPost
import com.vk.dto.common.id.UserId

interface PostLocalDataSource {
    suspend fun getPostList(id: UserId): List<RoomPost>
    suspend fun setPostList(postList: List<RoomPost>)
}