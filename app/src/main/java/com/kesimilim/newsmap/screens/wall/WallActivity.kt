package com.kesimilim.newsmap.screens.wall

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomPostWithAttachment
import com.kesimilim.newsmap.repository.PostRepository
import com.vk.dto.common.id.UserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WallActivity: AppCompatActivity(), WallAdapter.OnWallClickListener {
    init {
        NewsMapApplication.appComponent.inject(this)
    }
    @Inject
    lateinit var postRepository: PostRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wall)

        val value: Long = intent.getLongExtra("friendId", 0)
        GlobalScope.launch(Dispatchers.Main) {
            val postList = postRepository.fetchPostWithAttachmentList(UserId(value))
            setWall(postList)
        }
    }

    private fun setWall(wall: List<RoomPostWithAttachment>) {
        val wallAdapter = WallAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.wallRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        wallAdapter.setData(wall)
        recyclerView.adapter = wallAdapter
    }

    override fun setAttachment(attachmentList: List<RoomAttachment>, attachmentRecyclerView: RecyclerView) {
        val attachmentAdapter = AttachmentAdapter()
        attachmentRecyclerView.layoutManager = GridLayoutManager(this, 3)
        attachmentAdapter.setData(attachmentList)
        attachmentRecyclerView.adapter = attachmentAdapter
    }
}