package com.kesimilim.newsmap

import android.app.Application
import com.kesimilim.newsmap.di.AppComponent
import com.kesimilim.newsmap.di.DaggerAppComponent
import com.kesimilim.newsmap.di.module.DatabaseModule
import com.kesimilim.newsmap.di.module.RepositoryModule
import com.kesimilim.newsmap.screens.WelcomeActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import com.yandex.mapkit.MapKitFactory

class NewsMapApplication: Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initYandexMap()
        initVk()
        initDagger()
    }

    private fun initYandexMap() {
        MapKitFactory.setApiKey("da60e6fc-377b-4367-9a02-d5eaebc80e1a")
        MapKitFactory.initialize(this)
    }

    private fun initVk() {
        val tokenTracker = object: VKTokenExpiredHandler {
            override fun onTokenExpired() {
                WelcomeActivity.startFrom(this@NewsMapApplication)
            }
        }
        VK.addTokenExpiredHandler(tokenTracker)
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent
            .builder()
            .databaseModule(DatabaseModule(this))
            .repositoryModule(RepositoryModule(this))
            .build()
        appComponent.inject(this)
    }

    fun component(): DaggerAppComponent = appComponent as DaggerAppComponent

}