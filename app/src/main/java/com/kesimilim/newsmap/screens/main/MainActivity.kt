package com.kesimilim.newsmap.screens.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.databinding.ActivityMainBinding
import com.kesimilim.newsmap.dialogs.SelectMapDialog
import com.kesimilim.newsmap.model.*
import com.kesimilim.newsmap.repository.FriendRepository
import com.kesimilim.newsmap.repository.PostRepository
import com.kesimilim.newsmap.requests.VKUsersCommand
import com.kesimilim.newsmap.screens.welcome.WelcomeActivity
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import kotlinx.coroutines.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FriendsAdapter.OnFriendClickListener {

    init {
        NewsMapApplication.appComponent.inject(this)
    }

    @Inject lateinit var friendRepository: FriendRepository
    @Inject lateinit var postRepository: PostRepository

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoutBtn.setOnClickListener {
            VK.logout()
            WelcomeActivity.startFrom(this)
            finish()
        }

        binding.newsBtn.setOnClickListener {
            val dialog = SelectMapDialog(this)
            dialog.window?.setBackgroundDrawableResource(R.color.transparent)
            dialog.show()
        }
        requestUsers()
        showFriends()
    }

    private fun requestUsers() {
        VK.execute(VKUsersCommand(), object: VKApiCallback<List<VKUser>> {
            override fun success(result: List<VKUser>) {
                if (!isFinishing && !result.isEmpty()) {
                    val user = result[0]
                    binding.nameTV.text = "${user.firstName} ${user.lastName}"
                    binding.nameTV.setOnClickListener(createOnClickListener(user.id))

                    if (!TextUtils.isEmpty(user.photo)) {
                        Picasso.get()
                            .load(user.photo)
                            .error(R.drawable.user_placeholder)
                            .into(binding.avatarIV)
                    } else {
                        binding.avatarIV.setImageResource(R.drawable.user_placeholder)
                    }
                    binding.avatarIV.setOnClickListener(createOnClickListener(user.id))
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
        val adapter = FriendsAdapter(this)
        adapter.setData(friends)
        binding.friendsRV.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.friendsRV.adapter = adapter
    }

    override fun createOnClickListener(userId: Long) = View.OnClickListener {
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