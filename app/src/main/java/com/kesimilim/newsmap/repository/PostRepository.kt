package com.kesimilim.newsmap.repository

import com.kesimilim.newsmap.database.entity.RoomPost
import com.kesimilim.newsmap.database.entity.RoomPostWithAttachment
import com.kesimilim.newsmap.repository.postDataSource.local.PostLocalDataSource
import com.kesimilim.newsmap.repository.postDataSource.remote.PostRemoteDataSource
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository (
    private val postRemoteDataSource: PostRemoteDataSource,
    private val postLocalDataSource: PostLocalDataSource
) {
    suspend fun fetchPostList(id: UserId): List<RoomPost> = withContext(Dispatchers.IO) {
        postLocalDataSource.getPostList(id).ifEmpty {
            val postList = postRemoteDataSource.getPostList(id)
            postLocalDataSource.setPostList(postList)
            postList
        }
    }

    suspend fun fetchPostWithAttachmentList(id: UserId): List<RoomPostWithAttachment> = withContext(Dispatchers.IO) {
        postLocalDataSource.getPostAttachment(id).ifEmpty {
            postLocalDataSource.setPostList(postRemoteDataSource.getPostList(id))
            postLocalDataSource.getPostAttachment(id)
        }
    }
}