package com.kesimilim.newsmap.model

interface Post {
    val postId: Int?
    val ownerId: Long?
    val postText: String?
//    val attachment: List<Attachment>?
//    val copyHistory: List<CopyHistory>?
}