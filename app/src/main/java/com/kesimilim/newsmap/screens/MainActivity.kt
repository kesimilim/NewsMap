package com.kesimilim.newsmap.screens

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.DatabaseBuilder
import com.kesimilim.newsmap.database.entity.RoomAttachment
import com.kesimilim.newsmap.database.entity.RoomCopyHistory
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
import kotlinx.coroutines.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val database by lazy { DatabaseBuilder.getInstance(this) }
    var friendsData = arrayListOf<RoomFriend>()
    var userIdList = arrayListOf<UserId>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logoutButton: Button = findViewById(R.id.logoutBtn)
        logoutButton.setOnClickListener {
            VK.logout()

            WelcomeActivity.startFrom(this)
            finish()
        }

        val newsButton: Button = findViewById(R.id.newsBtn)
        newsButton.setOnClickListener {
            MapDialog(this).show()
        }

        requestUsers()
        showFriends()
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

    private suspend fun requestFriends(): List<RoomFriend> = withContext(Dispatchers.IO) {
        var vkUsers = arrayListOf<RoomFriend>()

        VK.execute(FriendsService().friendsGet(fields = listOf(
            UsersFields.PHOTO_200,
            UsersFields.CITY,
            UsersFields.HOME_TOWN,
            UsersFields.COUNTRY
        )), object: VKApiCallback<FriendsGetFieldsResponse> {


            override fun success(result: FriendsGetFieldsResponse) {
                val friends = result.items
                if (!isFinishing && friends.isNotEmpty()) {
                    friends.map { friend ->

                        userIdList.add(friend.id)
                        addInDatabase(friend)
                        val item = RoomFriend(
                            id = 0,
                            userId = friend.id.value,
                            firstName = friend.firstName ?: "",
                            lastName = friend.lastName ?: "",
                            photo = friend.photo200 ?: "",
                            city = RoomFriend.RoomCity(city(friend), 0.0, 0.0),
                            vkUserId = friend.id
                        )
                        vkUsers.add(item)
                    }
                }
            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })

        return@withContext vkUsers
    }

    private fun requestPost(id: UserId) {
        VK.execute(WallService().wallGetExtended(ownerId = id, count = 100), object: VKApiCallback<WallGetExtendedResponse> {
            override fun success(result: WallGetExtendedResponse) {
                val wall = result.items
                if (!isFinishing && wall.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch  {
                        wall.map { post ->
                            addPostInDatabase(id.value, post) // Database Action 2
                        }
                    }
                }
            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })
    }

    private fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        if (strAddress != "") {
            val coder = Geocoder(this)
            val address: List<Address>?
            try {
                address = coder.getFromLocationName(strAddress, 5)
                if (address == null) {
                    return null
                }
                val location: Address = address[0]
                return GeoPoint(
                    location.latitude,
                    location.longitude
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun addInDatabase(friend: UsersUserFull) {
        // Database Action 1
        val city = city(friend)
        val geo = getLocationFromAddress(city)
        var latitude = geo?.latitude
        var longitude = geo?.longitude

        if (city == "Uren") {
            latitude = 57.46
            longitude = 45.7847
        }

        if (latitude != 0.0 && latitude != null) {
            val item = RoomFriend(
                id = 0,
                userId = friend.id.value,
                firstName = friend.firstName ?: "",
                lastName = friend.lastName ?: "",
                photo = friend.photo200 ?: "",
                city = RoomFriend.RoomCity(city, latitude, longitude),
                vkUserId = friend.id
            )
            CoroutineScope(Dispatchers.IO).launch {
                database.friendsDao().addFriend(item)
            }
        }
    }

    private fun addPostInDatabase(userId: Long, post: WallWallpostFull) {
        val itemPost = RoomPost(
            id = 0,
            postId = post.id,
            userId = userId,
            postText = post.text,
            attachment = post.attachments != null,
            copyHistory = post.copyHistory != null
        )
        database.postDao().addPost(itemPost)

        Log.i(TAG, "Post added in database")

        if (itemPost.attachment) {
            post.attachments!!.map { attachment ->

                val itemAttachment = RoomAttachment(
                    id = 0,
                    postId = itemPost.postId,
                    photo = attachment.photo?.photo256
                )
                database.attachmentDao().addAttachment(itemAttachment)
                Log.i(TAG, "Attachment added in database")
//                    addAttachmentInDatabase(item.postId!!, attachment)
            }
        }
    }

    private fun addAttachmentInDatabase(postId: Int, attachments: WallWallpostAttachment) {
        CoroutineScope(Dispatchers.IO).launch  {
            val item = RoomAttachment(
                id = 0,
                postId = postId,
                photo = attachments.photo?.photo256
            )
            database.attachmentDao().addAttachment(item)
            Log.i(TAG, "Attachment added in database")
        }
    }

    private fun addCopyHistoryInDatabase(postId: Int, post: WallWallpostFull) {
        val item = RoomCopyHistory(
            id = 0,
            postId = postId,
            postText = post.text,
            attachment = post.attachments!!.isNotEmpty(),
        )

        CoroutineScope(Dispatchers.IO).launch {
            database.copyHistoryDao().addCopyHistory(item)
        }
        Log.i(TAG, "Copy history post added in database")

        if (item.attachment) {
            post.attachments!!.map { attachment ->
                addAttachmentInDatabase(item.postId!!, attachment)
            }
        }
    }

    private fun city(friend: UsersUserFull): String? {
        return if (friend.city != null) {
            friend.city?.title
        } else if (friend.homeTown != null) {
            friend.homeTown
        } else if (friend.country != null) {
            friend.country?.title
        } else {
            null
        }
    }

    private fun showFriends() {
        val adapter = FriendsAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.friendsRV)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        GlobalScope.launch(Dispatchers.Main) {
            val friends = requestFriends()

            adapter.setData(friends)
            recyclerView.adapter = adapter

            userIdList.map { id ->
                requestPost(id)
            }
        }
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