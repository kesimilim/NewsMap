package com.kesimilim.newsmap.model

data class Friends(
    val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val photo: String = "",
    val city: String = "",
    val location: GeoPoint,
    val deactivated: Boolean = false)
