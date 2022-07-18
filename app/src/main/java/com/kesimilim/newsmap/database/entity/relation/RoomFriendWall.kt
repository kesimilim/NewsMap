package com.kesimilim.newsmap.database.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.model.Post

data class RoomFriendWall(
    @Embedded
    val friend: RoomFriend,
    @Relation(
        parentColumn = "user_id",
        entity = Post::class,
        entityColumn = "post_owner_id"
    )
    val wall: List<Post>
)