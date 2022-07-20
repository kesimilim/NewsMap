package com.kesimilim.newsmap.repository.postDataSource.local

import com.kesimilim.newsmap.database.dao.PostDao
import com.kesimilim.newsmap.database.entity.RoomPost
import com.vk.dto.common.id.UserId

class PostLocalDataSourceImpl(private val postDao: PostDao): PostLocalDataSource {

    override suspend fun getPostList(id: UserId): List<RoomPost> = postDao.getPostById(id.value)

    override suspend fun setPostList(postList: List<RoomPost>) {
        postList.map { post ->
            postDao.addPost(post)
        }
    }
}