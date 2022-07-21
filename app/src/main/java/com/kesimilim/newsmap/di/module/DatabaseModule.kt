package com.kesimilim.newsmap.di.module

import android.content.Context
import androidx.room.Room
import com.kesimilim.newsmap.database.NewsMapDatabase
import com.kesimilim.newsmap.database.dao.AttachmentDao
import com.kesimilim.newsmap.database.dao.FriendsDao
import com.kesimilim.newsmap.database.dao.PostDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideDatabase(context: Context): NewsMapDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            NewsMapDatabase::class.java,
            "NewsAppDatabase")
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideFriendDao(database: NewsMapDatabase): FriendsDao = database.friendsDao()

    @Singleton
    @Provides
    fun providePostDao(database: NewsMapDatabase): PostDao = database.postDao()

    @Singleton
    @Provides
    fun provideAttachmentDao(database: NewsMapDatabase): AttachmentDao = database.attachmentDao()
}