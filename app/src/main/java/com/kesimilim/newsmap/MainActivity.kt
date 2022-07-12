package com.kesimilim.newsmap

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
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
import com.kesimilim.newsmap.database.entity.FriendsRoom
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
import com.vk.sdk.api.wall.WallService
import com.vk.sdk.api.wall.dto.WallGetExtendedResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val database by lazy { DatabaseBuilder.getInstance(this).FriendsDao() }
    var friendsData = arrayListOf<Friends>()

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
        val fields = listOf(UsersFields.PHOTO_200, UsersFields.CITY, UsersFields.HOME_TOWN, UsersFields.COUNTRY)
        VK.execute(FriendsService().friendsGet(fields = fields), object: VKApiCallback<FriendsGetFieldsResponse> {
            override fun success(result: FriendsGetFieldsResponse) {
                val friends = result.items
                if (!isFinishing && friends.isNotEmpty()) {
                    val vkUsers = friends.map { friend ->

                        val city = if (friend.city != null) {
                            friend.city?.title ?: ""
                        } else if (friend.homeTown != null) {
                            friend.homeTown ?: ""
                        } else if (friend.country != null) {
                            friend.city?.title ?: ""
                        } else {
                            ""
                        }
                        val location = getFriendLocation(city)
                        val postList = mutableListOf<FriendPost>()

                        requestPost(friend.id)

                        val item = FriendsRoom(
                            id = 0,
                            userId = friend.id.value,
                            firstName = friend.firstName ?: "",
                            lastName = friend.lastName ?: "",
                            photo = friend.photo200 ?: "",
                            city = city,
                            latitude = location.latitude,
                            longitude = location.longitude,
                            deactivated = friend.deactivated != null,
                            postList = postList
                        )
                        addInDatabase(item)

                        Friends(
                            id = friend.id.value,
                            firstName = friend.firstName ?: "",
                            lastName = friend.lastName ?: "",
                            photo = friend.photo200 ?: "",
                            city = city,
                            location = location,
                            deactivated = friend.deactivated != null
                        )
                    }
                    showFriends(vkUsers)
                }
            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })
    }

    private fun addInDatabase(item: FriendsRoom) {
        if (item.latitude != 0.0 && item.longitude != 0.0) {
            GlobalScope.launch(Dispatchers.IO) {
                database.addFriend(item)
                //showToast()
            }
        }
    }

    private fun showToast() {
        runOnUiThread {
            Toast.makeText(applicationContext, "Friend success added in Database", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFriendLocation(city: String): GeoPoint {
        if (city == "") return GeoPoint(0.0, 0.0)

        val geocoder = Geocoder(this)
        val address = geocoder.getFromLocationName(city, 5)
        val location: Address?

        if (address.isNotEmpty()) {
            location = address?.get(0)
        } else if (city == "Uren") {
            return GeoPoint(57.46, 45.7847)
        } else {
            return GeoPoint(0.0, 0.0)
        }
        return GeoPoint(location!!.latitude, location.longitude)
    }

    private fun showFriends(friends: List<Friends>) {
        friendsData = friends as ArrayList<Friends>

        val recyclerView = findViewById<RecyclerView>(R.id.friendsRV)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        val adapter = FriendsAdapter()
        adapter.setData(friends)

        recyclerView.adapter = adapter
    }

    private fun requestPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQ_CODE)
    }

    private fun requestPost(id: UserId) {
        VK.execute(WallService().wallGetExtended(ownerId = id, count = 100), object: VKApiCallback<WallGetExtendedResponse> {
            override fun success(result: WallGetExtendedResponse) {
                val wall = result.items
                if (!isFinishing && wall.isNotEmpty()) {
                    val posts = wall.map { post ->
                        post.text
                        /*
                        * val attachments: List<WallWallpostAttachment>? = null
                        * val copyHistory: List<WallWallpostFull>? = null,
                        * val postId: Int? = null,
                        * val text: String? = null,
                        * */
                    }
                }
            }
            override fun fail(error: Exception) {
                Log.e(TAG, error.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQ_CODE) {
            if (resultCode == RESULT_OK && data != null && data.data != null) {
                sharePost(Uri.parse(PathUtils.getPath(this, data.data!!)))
            } else {
                sharePost()
            }
        }
    }

    private fun sharePost(uri: Uri? = null) {
        //val messageField = findViewById<EditText>(R.id.messageET)

        val photos = ArrayList<Uri>()
        uri?.let {
            photos.add(it)
        }
//        VK.execute(VKWallPostCommand(messageField.text.toString(), photos), object: VKApiCallback<Int> {
//            override fun success(result: Int) {
//                Toast.makeText(this@MainActivity, R.string.wall_ok, Toast.LENGTH_SHORT).show()
//            }
//
//            override fun fail(error: Exception) {
//                Log.e(TAG, error.toString())
//            }
//        })
    }

    private fun createOnClickListener(userId: Long) = View.OnClickListener {
        VK.urlResolver.open(it.context, "https://vk.com/id$userId")
    }

    inner class FriendsAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val friends: MutableList<Friends> = arrayListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                = UserHolder(parent.context)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as UserHolder).bind(friends[position])
        }

        fun setData(friends: List<Friends>) {
            this.friends.clear()
            for (user in friends) {
                if (user.city != "") this.friends.add(user)
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
        private val latitudeTV: TextView = itemView.findViewById(R.id.latitude)
        private val longitudeTV: TextView = itemView.findViewById(R.id.longitude)

        fun bind(user: Friends) {
            nameTV.text = "${user.firstName} ${user.lastName}"
            //nameTV.setOnClickListener(createOnClickListener(user.id))
            if (!TextUtils.isEmpty(user.photo)) {
                Picasso.get().load(user.photo).error(R.drawable.user_placeholder).into(avatarIV)
            } else {
                avatarIV.setImageResource(R.drawable.user_placeholder)
            }
            //avatarIV.setOnClickListener(createOnClickListener(user.id))

            cityTV.text = user.city
            latitudeTV.text = "${user.location.latitude}"
            longitudeTV.text = "${user.location.longitude}"
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

    object PathUtils {
        fun getPath(context: Context, uri: Uri): String {
            if (uri.scheme == "file") {
                if (uri.path != null) return uri.path!!
                return ""
            }
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = context.contentResolver.query(uri, proj, null, null, null)
            val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return "file://" + cursor.getString(columnIndex)
        }
    }
}