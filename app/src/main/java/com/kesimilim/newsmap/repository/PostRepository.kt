package com.kesimilim.newsmap.repository

import com.kesimilim.newsmap.repository.postDataSource.local.PostLocalDataSource
import com.kesimilim.newsmap.repository.postDataSource.remote.PostRemoteDataSource

class PostRepository (
    private val postRemoteDataSource: PostRemoteDataSource,
    private val postLocalDataSource: PostLocalDataSource
) {

}