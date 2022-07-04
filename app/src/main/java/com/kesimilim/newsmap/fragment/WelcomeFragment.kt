package com.kesimilim.newsmap.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.databinding.FragmentWelcomeBinding
import com.vk.api.sdk.VK

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (VK.isLoggedIn()) {
            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
//            HomeActivity.startFrom(this)
//            finish()
            return
        }
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(view)

    }

}