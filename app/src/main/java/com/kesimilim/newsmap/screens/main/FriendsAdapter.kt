package com.kesimilim.newsmap.screens.main

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.squareup.picasso.Picasso

class FriendsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val friends: MutableList<RoomFriend> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = UserHolder(parent.context)

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
}

class UserHolder(context: Context?): RecyclerView.ViewHolder(
    LayoutInflater.from(context).inflate(R.layout.item_user, null)) {
    private val avatarIV: ImageView = itemView.findViewById(R.id.avatarIV)
    private val nameTV: TextView = itemView.findViewById(R.id.nameTV)
    private val cityTV: TextView = itemView.findViewById(R.id.city)

    fun bind(user: RoomFriend) {
        nameTV.text = "${user.firstName} ${user.lastName}"
        //nameTV.setOnClickListener(createOnClickListener(user.id))
        if (!TextUtils.isEmpty(user.photo)) {
            Picasso.get().load(user.photo).error(R.drawable.user_placeholder).into(avatarIV)
        } else {
            avatarIV.setImageResource(R.drawable.user_placeholder)
        }
        cityTV.text = user.city.name
    }
}