package com.kesimilim.newsmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView

class YandexMapFragment : Fragment(), MapObjectTapListener {

    lateinit var mapView: MapView

    private val latitude = 55.751574
    private val longitude = 47.573856

    private var placemarksClusterizedCollection: ClusterizedPlacemarkCollection? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_yandex_map, container, false)

        mapView = v.findViewById(R.id.mapview)

        mapView.map.move(
            CameraPosition(Point(56.63, 47.86), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0f),
            null
        )

//        friendsData.forEach { data ->
//            val placemark = placemarksClusterizedCollection?.addPlacemark(
//                Point(
//                    data.location.latitude,
//                    data.location.longitude
//                ),
//            )
//            placemark?.let {
//                it.addTapListener(this)
//            }
//        }

        return v
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

    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
        TODO("Not yet implemented")
    }



}