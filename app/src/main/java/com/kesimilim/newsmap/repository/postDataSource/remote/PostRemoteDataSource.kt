package com.kesimilim.newsmap.repository.postDataSource.remote

import com.kesimilim.newsmap.database.entity.RoomPost
import com.vk.dto.common.id.UserId

interface PostRemoteDataSource {
    suspend fun getPostList(id: UserId): List<RoomPost>
}