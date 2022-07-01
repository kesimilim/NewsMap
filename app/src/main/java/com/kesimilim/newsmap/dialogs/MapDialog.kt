package com.kesimilim.newsmap.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import com.kesimilim.newsmap.GoogleActivity
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.YandexActivity

class MapDialog(context: Context): Dialog(context){

    init {
        setCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_maps)

        val yandexButton: Button = findViewById(R.id.button_yandex_map)
        yandexButton.setOnClickListener {
            YandexActivity.startFrom(context)
        }

        val googleButton: Button = findViewById(R.id.button_google_map)
        googleButton.setOnClickListener {
            GoogleActivity.startFrom(context)
        }
    }

}