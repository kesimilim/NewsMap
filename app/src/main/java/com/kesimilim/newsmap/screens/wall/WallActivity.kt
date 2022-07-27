package com.kesimilim.newsmap.screens.wall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.database.entity.RoomPostWithAttachment
import com.kesimilim.newsmap.databinding.ActivityWallBinding
import com.kesimilim.newsmap.databinding.ItemPostBinding
import com.kesimilim.newsmap.repository.FriendRepository
import com.kesimilim.newsmap.repository.PostRepository
import com.squareup.picasso.Picasso
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WallActivity: AppCompatActivity(), WallAdapter.OnWallClickListener {
    init {
        NewsMapApplication.appComponent.inject(this)
    }
    @Inject lateinit var friendRepository: FriendRepository
    @Inject lateinit var postRepository: PostRepository
    private lateinit var binding: ActivityWallBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val value: Long = intent.getLongExtra("friendId", 0)
        GlobalScope.launch(Dispatchers.Main) {
            val user = friendRepository.fetchFriend(value)
            setUser(user)
            val postList = postRepository.fetchPostWithAttachmentList(UserId(value))
            setWall(postList)
        }
    }

    private fun setUser(user: RoomFriend) {
        if (!TextUtils.isEmpty(user.photo)) {
            Picasso.get().load(user.photo).error(R.drawable.user_placeholder).into(binding.avatarImageView)
        } else {
            binding.avatarImageView.setImageResource(R.drawable.user_placeholder)
        }
        binding.nameTextView.text = "${user.firstName} ${user.lastName}"
        binding.cityTextView.text = user.city.name
    }

    private fun setWall(wall: List<RoomPostWithAttachment>) {
        val wallAdapter = WallAdapter(this)
        wallAdapter.setData(wall)
        binding.wallRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.wallRecyclerView.adapter = wallAdapter
    }

    override fun setAttachment(attachmentList: List<RoomAttachment>) {
        val binding = ItemPostBinding.inflate(layoutInflater)
        val attachmentAdapter = AttachmentAdapter()
        binding.attachmentRecyclerView.layoutManager = GridLayoutManager(this, 3)
        attachmentAdapter.setData(attachmentList)
        binding.attachmentRecyclerView.adapter = attachmentAdapter
    }
}