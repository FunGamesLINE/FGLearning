package com.example.fglearning.fragments.viewList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fglearning.R
import com.example.fglearning.databinding.FragmentViewPackagesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewPackagesFragment : Fragment() {
    lateinit var binding: FragmentViewPackagesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackagesBinding.inflate(layoutInflater)

        binding.addPacketButton.setOnClickListener {

        }

        return binding.root
    }
}