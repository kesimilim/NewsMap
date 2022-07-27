package com.kesimilim.newsmap.screens.wall

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomPostWithAttachment
import com.kesimilim.newsmap.databinding.ItemPostBinding

class WallAdapter(private val listener: OnWallClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val postList: MutableList<RoomPostWithAttachment> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = PostHolder(ItemPostBinding.inflate(LayoutInflater.from(parent.context)), listener)

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

    class PostHolder(private val itemBinding: ItemPostBinding, private val listener: OnWallClickListener): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(user: RoomPostWithAttachment) {
            if (user.post.postText != null) {
                itemBinding.postTextView.visibility = View.VISIBLE
                itemBinding.postTextView.text = user.post.postText
            }
            if (user.post.copyHistoryText != null) {
                itemBinding.copyHistoryTextView.visibility = View.VISIBLE
                itemBinding.copyHistoryTextView.text = user.post.copyHistoryText
            }
            if (user.post.attachment) listener.setAttachment(user.attachmentList)
        }
    }

    interface OnWallClickListener {
        fun setAttachment(attachmentList: List<RoomAttachment>)
    }
}