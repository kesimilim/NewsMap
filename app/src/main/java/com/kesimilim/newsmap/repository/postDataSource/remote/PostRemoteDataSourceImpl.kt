package com.kesimilim.newsmap.repository.postDataSource.remote

import android.util.Log
import com.kesimilim.newsmap.database.dao.AttachmentDao
import com.kesimilim.newsmap.database.dao.CopyHistoryDao
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomCopyHistory
import com.kesimilim.newsmap.database.entity.RoomPost
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.photos.dto.PhotosPhoto
import com.vk.sdk.api.wall.WallService
import com.vk.sdk.api.wall.dto.WallGetExtendedResponse
import com.vk.sdk.api.wall.dto.WallWallpostFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.coroutines.suspendCoroutine

class PostRemoteDataSourceImpl(
    private val attachmentDao: AttachmentDao,
    private val copyHistoryDao: CopyHistoryDao
): PostRemoteDataSource {
    override suspend fun getPostList(id: UserId): List<RoomPost> = suspendCoroutine { list ->
        VK.execute(
            WallService().wallGetExtended(ownerId = id, count = 100),
            object: VKApiCallback<WallGetExtendedResponse> {
                override fun fail(error: Exception) {
                    list.resumeWith(Result.failure(error))
                }
                override fun success(result: WallGetExtendedResponse) {
                    val postList = arrayListOf<RoomPost>()
                    val wall = result.items

                    if (wall.isNotEmpty()) {
                        wall.map { post ->
                            val item = getRoomPost(post)
                            postList.add(item)
                        }
                        list.resumeWith(Result.success(postList))
                    }
                }
            }
        )
    }

    private fun getRoomPost(post: WallWallpostFull): RoomPost {
        val item = RoomPost(
            id = 0,
            userId = post.fromId?.value,
            postId = post.id,
            postText = post.text,
            attachment = post.attachments != null,
            copyHistory = post.copyHistory != null
        )

        if (item.attachment) {
            post.attachments?.map { attachment ->
                addAttachment(post.id!!, attachment.photo)
            }
        }

        if (item.copyHistory) {
            post.copyHistory?.map { copyHistory ->
                val itemCopyHistory = RoomCopyHistory(
                    id = 0,
                    postId = post.id,
                    postText = copyHistory.text,
                    attachment = copyHistory.attachments != null
                )

                GlobalScope.launch(Dispatchers.IO) {
                    copyHistoryDao.addCopyHistory(itemCopyHistory)
                }

                if (itemCopyHistory.attachment) {
                    copyHistory.attachments?.map { attachment ->
                        addAttachment(post.id!!, attachment.photo)
                    }
                }
            }
        }
        return item
    }

    private fun addAttachment(postId: Int, photo: PhotosPhoto?) {
        GlobalScope.launch(Dispatchers.IO) {
            attachmentDao.addAttachment(
                RoomAttachment(
                    id = 0,
                    postId = postId,
                    photo = photo?.sizes?.get(0)
                )
            )
        }
    }
}