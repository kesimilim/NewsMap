package com.kesimilim.newsmap.model

interface Friend {
    val userId: Long?
    val firstName: String
    val lastName: String
    val photo: String
    val city: City
}