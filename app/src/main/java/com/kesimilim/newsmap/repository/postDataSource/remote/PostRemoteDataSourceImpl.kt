package com.kesimilim.newsmap.repository.postDataSource.remote

import com.kesimilim.newsmap.database.entity.RoomPost
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.wall.WallService
import com.vk.sdk.api.wall.dto.WallGetExtendedResponse
import com.vk.sdk.api.wall.dto.WallWallpostFull
import kotlin.coroutines.suspendCoroutine

class PostRemoteDataSourceImpl: PostRemoteDataSource {
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
                            if (item != null) postList.add(item)
                        }
                        list.resumeWith(Result.success(postList))
                    }
                }
            }
        )
    }

    private fun getRoomPost(post: WallWallpostFull): RoomPost? {

        return null
    }
}