package com.kesimilim.newsmap.screens.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.dialogs.MapDialog
import com.kesimilim.newsmap.model.*
import com.kesimilim.newsmap.repository.FriendRepository
import com.kesimilim.newsmap.requests.VKUsersCommand
import com.kesimilim.newsmap.screens.WelcomeActivity
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import kotlinx.coroutines.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    init {
        NewsMapApplication.appComponent.inject(this)
    }

    @Inject lateinit var friendRepository: FriendRepository

//    private val database by lazy { DatabaseBuilder.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutButton: Button = findViewById(R.id.logoutBtn)
        logoutButton.setOnClickListener {
            VK.logout()

            WelcomeActivity.startFrom(this)
            finish()
        }

        val newsButton: Button = findViewById(R.id.newsBtn)
        newsButton.setOnClickListener {
            MapDialog(this).show()
        }

        requestUsers()
        showFriends()
    }

    private fun requestUsers() {
        VK.execute(VKUsersCommand(), object: VKApiCallback<List<VKUser>> {
            override fun success(result: List<VKUser>) {
                if (!isFinishing && !result.isEmpty()) {
                    val nameTV = findViewById<TextView>(R.id.nameTV)
                    val user = result[0]
                    nameTV.text = "${user.firstName} ${user.lastName}"
                    nameTV.setOnClickListener(createOnClickListener(user.id))

                    val avatarIV = findViewById<ImageView>(R.id.avatarIV)
                    if (!TextUtils.isEmpty(user.photo)) {
                        Picasso.get()
                            .load(user.photo)
                            .error(R.drawable.user_placeholder)
                            .into(avatarIV)
                    } else {
                        avatarIV.setImageResource(R.drawable.user_placeholder)
                    }
                    avatarIV.setOnClickListener(createOnClickListener(user.id))
                }
            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })
    }

    private fun showFriends() {
        GlobalScope.launch(Dispatchers.Main) {
            val friend = friendRepository.fetchFriendList()
            setFriend(friend)
        }
    }

    private fun setFriend(friends: List<RoomFriend>) {
        val adapter = FriendsAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.friendsRV)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        adapter.setData(friends)
        recyclerView.adapter = adapter
    }

    private fun createOnClickListener(userId: Long) = View.OnClickListener {
        VK.urlResolver.open(it.context, "https://vk.com/id$userId")
    }

    companion object {
        private const val TAG = "MainActivity"

        fun startFrom(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}