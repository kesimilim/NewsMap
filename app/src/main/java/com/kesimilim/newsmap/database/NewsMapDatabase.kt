package com.kesimilim.newsmap.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kesimilim.newsmap.database.dao.AttachmentDao
import com.kesimilim.newsmap.database.dao.CopyHistoryDao
import com.kesimilim.newsmap.database.dao.FriendsDao
import com.kesimilim.newsmap.database.dao.PostDao
import com.kesimilim.newsmap.database.dao.relation.CopyHistoryAttachmentDao
import com.kesimilim.newsmap.database.dao.relation.FriendWallDao
import com.kesimilim.newsmap.database.dao.relation.PostAttachmentDao
import com.kesimilim.newsmap.database.dao.relation.PostCopyHistoryDao
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomCopyHistory
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.database.entity.RoomPost
import com.kesimilim.newsmap.database.entity.relation.RoomCopyHistoryAttachment
import com.kesimilim.newsmap.database.entity.relation.RoomFriendWall
import com.kesimilim.newsmap.database.entity.relation.RoomPostAttachment
import com.kesimilim.newsmap.database.entity.relation.RoomPostCoryHistory

@Database(
    entities = [
        RoomAttachment::class,
        RoomCopyHistory::class,
        RoomFriend::class,
        RoomPost::class,
               ],
    version = 1
)
@TypeConverters(Converter::class)
abstract class NewsMapDatabase: RoomDatabase() {
    abstract fun friendsDao(): FriendsDao
    abstract fun postDao(): PostDao
    abstract fun copyHistoryDao(): CopyHistoryDao
    abstract fun attachmentDao(): AttachmentDao

    abstract fun copyHistoryAttachmentDao(): CopyHistoryAttachmentDao
    abstract fun friendWallDao(): FriendWallDao
    abstract fun postAttachmentDao(): PostAttachmentDao
    abstract fun postCopyHistoryDao(): PostCopyHistoryDao
}