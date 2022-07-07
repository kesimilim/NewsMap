package com.kesimilim.newsmap.fragment

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.activity.HomeActivity
import com.kesimilim.newsmap.databinding.FragmentWelcomeBinding
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.exceptions.VKAuthException

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var authLauncher: ActivityResultLauncher<Collection<VKScope>>
    private lateinit var binding: FragmentWelcomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (VK.isLoggedIn()) {
            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
        }
        super.onViewCreated(view, savedInstanceState)

        authLauncher = VK.login() { result : VKAuthenticationResult ->
            when (result) {
                is VKAuthenticationResult.Success -> findNavController().navigate()
                is VKAuthenticationResult.Failed -> onLoginFailed(result.exception)
            }
        }


            val message = " HI! :3 "
            val action = WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment(message)

            findNavController().navigate(action)


        }

    }

    private fun onLogin() {
        findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)

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

}