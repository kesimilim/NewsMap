package com.kesimilim.newsmap.screens.maps.google

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.entity.RoomFriend
import com.kesimilim.newsmap.dialogs.FriendListDialog
import com.kesimilim.newsmap.repository.FriendRepository
import com.kesimilim.newsmap.screens.wall.WallActivity
import kotlinx.coroutines.*
import javax.inject.Inject

class GoogleActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

    init {
        NewsMapApplication.appComponent.inject(this)
    }
    @Inject lateinit var friendRepository: FriendRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)

        GlobalScope.launch(Dispatchers.Main) {
            val list = mapObject(friendRepository.fetchFriendList())
            showFragment(list)
        }
    }

    private fun showFragment(list: List<List<RoomFriend>>) {
        val callback = OnMapReadyCallback { googleMap ->
            googleMap.setOnMarkerClickListener(this)
            list.map { friendsList ->
                val friend = friendsList[0]
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(friend.city.latitude!!, friend.city.longitude!!))
                        .title("${friend.firstName} ${friend.lastName}")
                ).let {
                    it?.tag = friendsList
                }
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(56.633331, 47.866669)))
        }

        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.isVisible
        mapFragment.getMapAsync(callback)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val data: List<RoomFriend> = marker.tag as List<RoomFriend>
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

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, GoogleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}