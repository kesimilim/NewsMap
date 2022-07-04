package com.kesimilim.newsmap

import android.app.Application
import com.kesimilim.newsmap.activity.WelcomeActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.yandex.mapkit.MapKitFactory

class NewsMapApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("da60e6fc-377b-4367-9a02-d5eaebc80e1a")
        MapKitFactory.initialize(this)
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private val tokenTracker = object: VKTokenExpiredHandler {
        override fun onTokenExpired() {
            WelcomeActivity.startFrom(this@NewsMapApplication)
        }
    }
}