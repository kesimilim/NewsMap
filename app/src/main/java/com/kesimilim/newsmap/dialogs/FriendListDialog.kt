package com.kesimilim.newsmap.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.screens.wall.WallActivity
import com.squareup.picasso.Picasso
import com.vk.dto.common.id.UserId

class FriendListDialog (context: Context, private val friendList: List<RoomFriend>): Dialog(context) {

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_friend_list)

        val adapter = MapFriendAdapter()
        val recyclerView:RecyclerView = findViewById(R.id.mapFriendRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setData(friendList)
        recyclerView.adapter = adapter
    }

    private fun mapItemOnClickListener(userId: UserId) = View.OnClickListener {
        val intent = Intent(context, WallActivity::class.java)
        intent.putExtra("friendId", userId.value)
        context.startActivity(intent)
    }

    inner class MapFriendAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val friendList: MutableList<RoomFriend> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = MapFriendHolder(parent.context)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as MapFriendHolder).bind(friendList[position])
        }

        fun setData(friendList: List<RoomFriend>) {
            this.friendList.clear()
            for (friend in friendList) {
                this.friendList.add(friend)
            }
        }

        override fun getItemCount(): Int = friendList.size

        inner class MapFriendHolder(context: Context?): RecyclerView.ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_friend, null)
        ) {
            private val avatar: ImageView = itemView.findViewById(R.id.avatarImageView)
            private val name: TextView = itemView.findViewById(R.id.nameTextView)
            private val city: TextView = itemView.findViewById(R.id.cityTextView)
            private val item: LinearLayout = itemView.findViewById(R.id.mapFriendItem)

            fun bind(friend: RoomFriend) {
                item.setOnClickListener(mapItemOnClickListener(friend.vkUserId))
                name.text = "${friend.firstName} ${friend.lastName}"
                city.text = friend.city.name
                if (!TextUtils.isEmpty(friend.photo)) {
                    Picasso.get().load(friend.photo).error(R.drawable.user_placeholder).into(avatar)
                } else {
                    avatar.setImageResource(R.drawable.user_placeholder)
                }
            }
        }
    }
}