package com.kesimilim.newsmap.di.module

import android.content.Context
import com.kesimilim.newsmap.database.dao.FriendsDao
import com.kesimilim.newsmap.database.dao.PostDao
import com.kesimilim.newsmap.repository.FriendRepository
import com.kesimilim.newsmap.repository.PostRepository
import com.kesimilim.newsmap.repository.friendDataSource.local.FriendLocalDataSource
import com.kesimilim.newsmap.repository.friendDataSource.local.FriendLocalDataSourceImpl
import com.kesimilim.newsmap.repository.friendDataSource.remote.FriendRemoteDataSource
import com.kesimilim.newsmap.repository.friendDataSource.remote.FriendRemoteDataSourceImpl
import com.kesimilim.newsmap.repository.postDataSource.local.PostLocalDataSource
import com.kesimilim.newsmap.repository.postDataSource.local.PostLocalDataSourceImpl
import com.kesimilim.newsmap.repository.postDataSource.remote.PostRemoteDataSource
import com.kesimilim.newsmap.repository.postDataSource.remote.PostRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val context: Context) {
    // Friend Repository
    @Singleton
    @Provides
    fun provideFriendRemoteDataSource(): FriendRemoteDataSource =
        FriendRemoteDataSourceImpl(context)

    @Singleton
    @Provides
    fun provideFriendLocalDataSource(friendDao: FriendsDao): FriendLocalDataSource =
        FriendLocalDataSourceImpl(friendDao)

    @Singleton
    @Provides
    fun provideFriendRepository(
        remote: FriendRemoteDataSource,
        local: FriendLocalDataSource
    ): FriendRepository = FriendRepository(remote, local)

    // Post Repository
    @Singleton
    @Provides
    fun providePostRepository(
        remote: PostRemoteDataSource,
        local: PostLocalDataSource
    ): PostRepository = PostRepository(remote, local)

    @Singleton
    @Provides
    fun providePostRemoteDataSource(): PostRemoteDataSource = PostRemoteDataSourceImpl()

    @Singleton
    @Provides
    fun providePostLocalDataSource(postDao: PostDao): PostLocalDataSource =
        PostLocalDataSourceImpl(postDao)


}