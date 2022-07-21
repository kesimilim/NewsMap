package com.kesimilim.newsmap.screens.maps.yandex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.repository.FriendRepository
import com.kesimilim.newsmap.screens.wall.WallActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class YandexActivity : AppCompatActivity(), MapObjectTapListener {

    init {
        NewsMapApplication.appComponent.inject(this)
    }

    @Inject lateinit var friendRepository: FriendRepository

    lateinit var mapView: MapView
    lateinit var mapObjects: MapObjectCollection;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yandex)

        mapView = findViewById(R.id.mapview)
        mapObjects = mapView.map.mapObjects.addCollection()

        GlobalScope.launch(Dispatchers.Main) {
            val friendsList = friendRepository.fetchFriendList()
            for (friend in friendsList) {
                addPoint(friend)
            }
        }
    }

    private fun addPoint(friend: RoomFriend) {
        mapObjects.addPlacemark(
            Point(
                friend.city.latitude!!,
                friend.city.longitude!!
            ),
            ImageProvider.fromResource(applicationContext, R.drawable.marker)
        ).let {
            it.userData = friend
            it.addTapListener(this)
        }
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onMapObjectTap(friend: MapObject, point: Point): Boolean {
        val data: RoomFriend = friend.userData as RoomFriend
        Toast.makeText(this,  "user -> ${data.firstName} ${data.lastName} ", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, WallActivity::class.java)
        intent.putExtra("friendId", data.vkUserId.value)
        this.startActivity(intent)
        return true
    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, YandexActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }

}