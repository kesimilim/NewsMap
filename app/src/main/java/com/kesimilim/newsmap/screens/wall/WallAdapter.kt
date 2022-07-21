package com.kesimilim.newsmap.screens.wall

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomPostWithAttachment

class WallAdapter(private val listener: OnWallClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val postList: MutableList<RoomPostWithAttachment> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostHolder(parent.context, listener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostHolder).bind(postList[position])
    }

    fun setData(data: List<RoomPostWithAttachment>) {
        this.postList.clear()
        for (user in data) {
            this.postList.add(user)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = postList.size

    class PostHolder(context: Context?, private val listener: OnWallClickListener): RecyclerView.ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_post, null)) {
        private val postText: TextView = itemView.findViewById(R.id.postTextView)
        private val copyHistoryText: TextView = itemView.findViewById(R.id.copyHistoryTextView)
        private val attachmentRecyclerView: RecyclerView = itemView.findViewById(R.id.photoRecyclerView)

        fun bind(user: RoomPostWithAttachment) {
            if (user.post.postText != null) {
                postText.visibility = View.VISIBLE
                postText.text = user.post.postText
            }
            if (user.post.copyHistoryText != null) {
                copyHistoryText.visibility = View.VISIBLE
                copyHistoryText.text = user.post.copyHistoryText
            }
            if (user.post.attachment) listener.setAttachment(user.attachmentList, attachmentRecyclerView)
        }
    }

    interface OnWallClickListener {
        fun setAttachment(attachmentList: List<RoomAttachment>, attachmentRecyclerView: RecyclerView)
    }
}