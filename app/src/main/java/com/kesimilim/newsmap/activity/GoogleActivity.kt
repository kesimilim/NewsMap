package com.kesimilim.newsmap.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kesimilim.newsmap.fragment.GoogleMapFragment
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.DatabaseBuilder

class GoogleActivity : AppCompatActivity() {

    private val database by lazy { DatabaseBuilder.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, GoogleMapFragment(database))
            .addToBackStack(null)
            .commit()

    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, GoogleActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}