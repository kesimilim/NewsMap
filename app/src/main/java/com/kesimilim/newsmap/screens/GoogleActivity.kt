package com.kesimilim.newsmap.screens

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.repository.FriendRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoogleActivity : AppCompatActivity() {

    init {
        NewsMapApplication.appComponent.inject(this)
    }

    @Inject
    lateinit var friendRepository: FriendRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)

        GlobalScope.launch(Dispatchers.Main) {
            val friend = friendRepository.fetchFriendList()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, GoogleMapFragment(friend))
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, GoogleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}