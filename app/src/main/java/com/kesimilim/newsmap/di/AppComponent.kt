package com.kesimilim.newsmap.di

import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.di.module.DatabaseModule
import com.kesimilim.newsmap.di.module.RepositoryModule
import com.kesimilim.newsmap.screens.maps.google.GoogleActivity
import com.kesimilim.newsmap.screens.maps.yandex.YandexActivity
import com.kesimilim.newsmap.screens.main.MainActivity
import com.kesimilim.newsmap.screens.wall.WallActivity
import com.kesimilim.newsmap.screens.welcome.WelcomeActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RepositoryModule::class, DatabaseModule::class])
interface AppComponent {

    fun inject(application: NewsMapApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(yandexActivity: YandexActivity)
    fun inject(googleActivity: GoogleActivity)
    fun inject(wallActivity: WallActivity)
    fun inject(welcomeActivity: WelcomeActivity)
}