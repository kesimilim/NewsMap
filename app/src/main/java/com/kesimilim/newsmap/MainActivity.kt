package com.kesimilim.newsmap

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.database.DatabaseBuilder
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.database.entity.RoomPost
import com.kesimilim.newsmap.dialogs.MapDialog
import com.kesimilim.newsmap.model.*
import com.kesimilim.newsmap.requests.VKUsersCommand
import com.squareup.picasso.Picasso
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.friends.FriendsService
import com.vk.sdk.api.friends.dto.FriendsGetFieldsResponse
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserFull
import com.vk.sdk.api.wall.WallService
import com.vk.sdk.api.wall.dto.WallGetExtendedResponse
import com.vk.sdk.api.wall.dto.WallWallpostAttachment
import com.vk.sdk.api.wall.dto.WallWallpostFull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val database by lazy { DatabaseBuilder.getInstance(this).FriendsDao() }
    var friendsData = arrayListOf<RoomFriend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutButton: Button = findViewById(R.id.logoutBtn)
        logoutButton.setOnClickListener {
            VK.logout()

            WelcomeActivity.startFrom(this)
            finish()
        }
//        val shareButton: Button = findViewById(R.id.shareBtn)
//        shareButton.setOnClickListener {
//            requestPhoto()
//        }

        val newsButton: Button = findViewById(R.id.newsBtn)
        newsButton.setOnClickListener {
//            val intent = Intent(this, YandexActivity::class.java)
//            intent.putExtra("FRIENDS_LIST", friendsData)
//            YandexActivity.startFrom(this)
            MapDialog(this).show()
        }

        requestUsers()
        requestFriends()
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

    private fun requestFriends() {
        VK.execute(FriendsService().friendsGet(fields = listOf(
            UsersFields.PHOTO_200,
            UsersFields.CITY,
            UsersFields.HOME_TOWN,
            UsersFields.COUNTRY
        )), object: VKApiCallback<FriendsGetFieldsResponse> {
            override fun success(result: FriendsGetFieldsResponse) {
                val friends = result.items
                if (!isFinishing && friends.isNotEmpty()) {
                    val vkUsers = friends.map { friend ->
                        RoomFriend(
                            firstName = friend.firstName ?: "",
                            lastName = friend.lastName ?: "",
                            photo = friend.photo200 ?: "",
                            city = RoomFriend.RoomCity(city(friend))
                        ).also { addInDatabase(friend) }
                    }
                    showFriends(vkUsers)
                }
            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })
    }

    private fun requestPost(id: UserId) {
        VK.execute(WallService().wallGetExtended(ownerId = id, count = 100), object: VKApiCallback<WallGetExtendedResponse> {
            override fun success(result: WallGetExtendedResponse) {
                val wall = result.items
                if (!isFinishing && wall.isNotEmpty()) {
                    wall.map { post ->
                        RoomPost(
                            postId = post.postId,
                            ownerId = post.ownerId?.value,
                            postText = post.text
                        )
                        addPostInDatabase(post) // Database Action 2

                        if (post.attachments!!.isNotEmpty()) {
                            post.attachments?.map { attachment ->
                                addAttachmentInDatabase(attachment) // Database Action 2*
                            }
                        }

                        if (post.copyHistory!!.isNotEmpty()) {
                            post.copyHistory?.map { copyHistory ->
                                addPostInDatabase(copyHistory) // Database Action 3
                                if (copyHistory.attachments!!.isNotEmpty()) {
                                    copyHistory.attachments?.map { attachment ->
                                        addAttachmentInDatabase(attachment) // Database Action 3*
                                    }
                                }
                            }
                        }
                    }
                }
            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })
    }

    private fun addInDatabase(friend: UsersUserFull) {
        // Database Action 1
        val item = RoomFriend(
            userId = friend.id.value,
            firstName = friend.firstName ?: "",
            lastName = friend.lastName ?: "",
            photo = friend.photo200 ?: "",
            city = RoomFriend.RoomCity(city(friend)).apply { this.setLocation(this@MainActivity) }
        )

        if (item.city.latitude != 0.0 && item.city.longitude != 0.0) {
            GlobalScope.launch(Dispatchers.IO) {
                database.addFriend(item)
            }
        }

        requestPost(friend.id)
    }

    private fun addPostInDatabase(post: WallWallpostFull) {

    }

    private fun addAttachmentInDatabase(attachments: WallWallpostAttachment) {

    }

    private fun city(friend: UsersUserFull): String {
        return if (friend.city != null) {
            friend.city?.title ?: ""
        } else if (friend.homeTown != null) {
            friend.homeTown ?: ""
        } else if (friend.country != null) {
            friend.country?.title ?: ""
        } else {
            ""
        }
    }

    private fun showFriends(friends: List<RoomFriend>) {
        friendsData = friends as ArrayList<RoomFriend>

        val recyclerView = findViewById<RecyclerView>(R.id.friendsRV)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val adapter = FriendsAdapter()
        adapter.setData(friends)

        recyclerView.adapter = adapter
    }

    private fun createOnClickListener(userId: Long) = View.OnClickListener {
        VK.urlResolver.open(it.context, "https://vk.com/id$userId")
    }

    inner class FriendsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
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

    inner class UserHolder(context: Context?): RecyclerView.ViewHolder(
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

    companion object {
        private const val TAG = "MainActivity"

        private const val IMAGE_REQ_CODE = 101

        fun startFrom(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}