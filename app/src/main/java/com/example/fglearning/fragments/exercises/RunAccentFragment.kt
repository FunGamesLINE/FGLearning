package com.example.fglearning.fragments.exercises

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fglearning.R
import com.example.fglearning.databinding.FragmentRunAccentBinding

class RunAccentFragment : Fragment() {
    lateinit var binding: FragmentRunAccentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunAccentBinding.inflate(layoutInflater)

        return binding.root
    }
}