package com.kesimilim.newsmap.screens.welcome

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.kesimilim.newsmap.NewsMapApplication
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.database.NewsMapDatabase
import com.kesimilim.newsmap.screens.main.MainActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeActivity : AppCompatActivity() {

    init {
        NewsMapApplication.appComponent.inject(this)
    }

    @Inject lateinit var database: NewsMapDatabase
    private lateinit var authLauncher: ActivityResultLauncher<Collection<VKScope>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (VK.isLoggedIn()) {
            MainActivity.startFrom(this)
            finish()
            return
        }
        setContentView(R.layout.activity_welcome)

        GlobalScope.launch(Dispatchers.IO) {
            database.clearAllTables()
        }

        authLauncher = VK.login(this) { result : VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> onLogin()
                is VKAuthenticationResult.Failed -> onLoginFailed(result.exception)
            }
        }

        val loginButton: Button = findViewById(R.id.loginBtn)
        loginButton.setOnClickListener {
            authLauncher.launch(arrayListOf(VKScope.WALL, VKScope.PHOTOS))
        }
    }

    private fun onLogin() {
        MainActivity.startFrom(this)
        finish()
    }

    private fun onLoginFailed(exception: VKAuthException) {
        if (!exception.isCanceled) {
            val descriptionResourse =
                if (exception.webViewError == WebViewClient.ERROR_HOST_LOOKUP) R.string.message_connection_error
                else R.string.message_unknown_error

            AlertDialog.Builder(this)
                .setMessage(descriptionResourse)
                .setPositiveButton(com.vk.api.sdk.R.string.vk_retry) { _, _ ->
                    authLauncher.launch(arrayListOf(VKScope.WALL, VKScope.PHOTOS))
                }
                .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    companion object {
        fun startFrom(context: Context) {
            val intent = Intent(context, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        }
    }
}