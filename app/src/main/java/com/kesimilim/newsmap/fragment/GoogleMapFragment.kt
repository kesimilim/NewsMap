package com.kesimilim.newsmap.fragment

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.NewsMapDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GoogleMapFragment(database: NewsMapDatabase) : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->
        GlobalScope.launch(Dispatchers.IO) {
            val friendsList = database.FriendsDao().getAllFriends()
            withContext(Dispatchers.Main) {
                for (friend in friendsList) {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(friend.latitude, friend.longitude))
                            .title("${friend.firstName} ${friend.lastName}")
                    )
                }
            }
        }

//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(56.633331, 47.866669)))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_google_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}