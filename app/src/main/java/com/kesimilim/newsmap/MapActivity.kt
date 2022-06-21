package com.kesimilim.newsmap

import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.yandex.mapkit.MapKitFactory
import java.util.*

class MapActivity : AppCompatActivity() {

    private lateinit var googleMapButton: Button
    lateinit var yandexMapButton: Button

    lateinit var text: TextView

    private val latitude = 56.63
    private val longitude = 47.86

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("da60e6fc-377b-4367-9a02-d5eaebc80e1a")
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_map)
        /////////////////////////////////////

        googleMapButton = findViewById(R.id.google_map_button)
        yandexMapButton = findViewById(R.id.yandex_map_button)

        text = findViewById(R.id.text)
        text.text = getCityName(latitude, longitude)

        googleMapButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, GoogleMapFragment())
                .addToBackStack(null)
                .commit()

            text.text = getCityName(latitude, longitude)
        }

        yandexMapButton.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, YandexMapFragment())
                .addToBackStack(null)
                .commit()
        }


    }

    private fun getCityName(lat: Double, long: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, long, 1)

        return addresses[0].locality
    }


}