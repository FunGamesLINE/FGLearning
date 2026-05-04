package com.example.fglearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fglearning.databinding.FragmentViewPackageBinding

class ViewPackageFragment : Fragment() {
    lateinit var binding: FragmentViewPackageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackageBinding.inflate(layoutInflater)

        //binding.

        return binding.root
    }
}