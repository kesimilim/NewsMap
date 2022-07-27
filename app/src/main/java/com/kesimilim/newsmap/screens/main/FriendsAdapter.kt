package com.kesimilim.newsmap.screens.main

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.databinding.ItemUserBinding
import com.squareup.picasso.Picasso

class FriendsAdapter(private val listener: OnFriendClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val friends: MutableList<RoomFriend> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = UserHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context)), listener)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as UserHolder).bind(friends[position])
    }

    fun setData(friends: List<RoomFriend>) {
        this.friends.clear()
        for (user in friends) {
            if (user.city.name != "") this.friends.add(user)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount() = friends.size

    class UserHolder(private val itemBinding: ItemUserBinding, private val listener: OnFriendClickListener): RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(user: RoomFriend) {
            itemBinding.avatarIV.setOnClickListener(listener.createOnClickListener(user.vkUserId.value))
            itemBinding.nameTV.text = "${user.firstName} ${user.lastName}"
            if (!TextUtils.isEmpty(user.photo)) {
                Picasso.get().load(user.photo).error(R.drawable.user_placeholder).into(itemBinding.avatarIV)
            } else {
                itemBinding.avatarIV.setImageResource(R.drawable.user_placeholder)
            }
            itemBinding.city.text = user.city.name
        }
    }

    interface OnFriendClickListener {
        fun createOnClickListener(userId: Long): View.OnClickListener
    }
}