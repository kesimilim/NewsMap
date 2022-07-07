package com.kesimilim.newsmap.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.kesimilim.newsmap.R
import com.kesimilim.newsmap.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val arguments: HomeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        binding.nameTV.text = arguments.message

        binding.buttonGoogleMap.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_googleMapFragment)
        }

        binding.buttonYandexMap.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_yandexMapFragment)
        }

    }

}