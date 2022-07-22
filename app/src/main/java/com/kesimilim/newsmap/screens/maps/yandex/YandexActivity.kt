package com.kesimilim.newsmap.screens.maps.yandex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.dialogs.FriendListDialog
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
            val list = mapObject(friendRepository.fetchFriendList())
            list.map { friendsList ->
                addPoint(friendsList)
            }
        }
    }

    private fun mapObject(list: List<RoomFriend>): List<List<RoomFriend>> {
        val listItem1 = arrayListOf<List<RoomFriend>>()
        for (item1 in list) {
            val listItem2 = arrayListOf<RoomFriend>()
            var i = 0
            for (item2 in list) {
                if (item1.city.latitude == item2.city.latitude && item1.city.longitude == item2.city.longitude) {
                    listItem2.add(item2)
                    i += 1
                }
            }
            if (i > 1) {
                listItem1.add(listItem2)
            } else {
                listItem1.add(listOf(item1))
            }
        }
        return listItem1.distinct().toList()
    }

    private fun addPoint(list: List<RoomFriend>) {
        val friend = list[0]
        mapObjects.addPlacemark(
            Point(
                friend.city.latitude!!,
                friend.city.longitude!!
            ),
            ImageProvider.fromResource(applicationContext, R.drawable.marker)
        ).let {
            it.userData = list
            it.addTapListener(this)
        }
    }

    override fun onMapObjectTap(friend: MapObject, point: Point): Boolean {
        val data: List<RoomFriend> = friend.userData as List<RoomFriend>
        if (data.size > 1) {
            val dialog = FriendListDialog(this, data)
            dialog.window?.setBackgroundDrawableResource(R.color.transparent)
            dialog.show()
        } else {
            val intent = Intent(this, WallActivity::class.java)
            intent.putExtra("friendId", data[0].vkUserId.value)
            this.startActivity(intent)
        }
        return true
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

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, YandexActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }

}