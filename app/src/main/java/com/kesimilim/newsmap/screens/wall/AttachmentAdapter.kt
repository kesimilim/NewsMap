package com.kesimilim.newsmap.screens.wall

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.squareup.picasso.Picasso

class AttachmentAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val attachmentList: MutableList<RoomAttachment> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = AttachmentHolder(parent.context)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AttachmentHolder).bind(attachmentList[position])
    }

    fun setData(data: List<RoomAttachment>) {
        this.attachmentList.clear()
        for (user in data) {
            this.attachmentList.add(user)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = attachmentList.size

    class AttachmentHolder(context: Context?): RecyclerView.ViewHolder(
        LayoutInflater.from(context).inflate(R.layout.item_attachment, null)) {
        private val postImage: ImageView = itemView.findViewById(R.id.postImageView)
        fun bind(attachment: RoomAttachment) {
            if (!TextUtils.isEmpty(attachment.photo?.url)) {
                Picasso.get()
                    .load(attachment.photo?.url)
                    .error(R.drawable.user_placeholder)
                    .into(postImage)
            } else {
                postImage.visibility = View.GONE
            }
        }
    }
}