package com.example.fglearning.fragments.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fglearning.R
import com.example.fglearning.databinding.FragmentRunFlashcardsBinding

class RunFlashcardsFragment : Fragment() {
    lateinit var binding: FragmentRunFlashcardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunFlashcardsBinding.inflate(layoutInflater)

        return binding.root
    }
}