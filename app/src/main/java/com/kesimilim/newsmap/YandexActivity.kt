package com.kesimilim.newsmap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kesimilim.newsmap.database.DatabaseBuilder
import com.kesimilim.newsmap.database.entity.FriendsRoom
import com.kesimilim.newsmap.model.Friends
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class YandexActivity : AppCompatActivity(), MapObjectTapListener {

    lateinit var mapView: MapView
    lateinit var mapObjects: MapObjectCollection;
    private val database by lazy { DatabaseBuilder.getInstance(this).FriendsDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yandex)

        mapView = findViewById(R.id.mapview)
        mapObjects = mapView.map.mapObjects.addCollection()

        GlobalScope.launch(Dispatchers.IO) {
            val friendsList = database.getAllFriends()

            withContext(Dispatchers.Main) {
                for (friend in friendsList) {
                    addPoint(friend)
                }
            }
        }

    }

    private fun addPoint(friend: FriendsRoom) {
        mapObjects.addPlacemark(
            Point(
                friend.latitude,
                friend.longitude
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
        val data: FriendsRoom = friend.userData as FriendsRoom
        Toast.makeText(this, "${data.firstName} ${data.lastName}", Toast.LENGTH_SHORT).show()
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