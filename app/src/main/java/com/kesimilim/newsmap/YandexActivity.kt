package com.kesimilim.newsmap

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kesimilim.newsmap.database.DatabaseBuilder
import com.kesimilim.newsmap.database.entity.FriendsRoom
import com.kesimilim.newsmap.model.Friends
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class YandexActivity : AppCompatActivity() {

    lateinit var mapView: MapView
    lateinit var mapObjects: MapObjectCollection;
    lateinit var placemarkMapObject: PlacemarkMapObject
    private val database by lazy { DatabaseBuilder.getInstance(this).FriendsDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("da60e6fc-377b-4367-9a02-d5eaebc80e1a")
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_yandex)

        mapView = findViewById(R.id.mapview)
        mapView.map.move(
            CameraPosition(Point(56.63, 47.86), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )
        mapObjects = mapView.map.mapObjects.addCollection()

        var friendsList: List<FriendsRoom> = mutableListOf()

        GlobalScope.launch(Dispatchers.IO) {
            addPoint(database.getAllFriends())
        }

    }

    private fun addPoint(list: List<FriendsRoom>) {
        GlobalScope.launch(Dispatchers.Main) {
            for (friend in list) {
                mapObjects.addPlacemark(
                    Point(friend.latitude, friend.latitude),
                    ImageProvider.fromResource(applicationContext, R.drawable.marker)
                )
            }
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

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, YandexActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }

}