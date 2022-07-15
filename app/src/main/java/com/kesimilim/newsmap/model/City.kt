package com.kesimilim.newsmap.model

import android.content.Context

interface City {
    val name: String?
    val latitude: Double?
    val longitude: Double?

    fun setLocation(context: Context)
}