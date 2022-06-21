package com.kesimilim.newsmap

import android.app.Application
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler

class NewsMapApplication: Application() {

    private val ACCESS_TOKEN = "b082f5afb082f5afb082f5afa5b0fe042cbb082b082f5afd222a646c6a636a75a15bca5"

    override fun onCreate() {
        super.onCreate()
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            WelcomeActivity.startFrom(this@NewsMapApplication)
        }

    }



}