package com.example.fglearning.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.fglearning.R
import com.example.fglearning.databinding.FragmentResultsBinding
import com.example.fglearning.viewmodel.ExerciseViewModel
import com.example.fglearning.viewmodel.PackageItemsViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlin.getValue

class ResultsFragment : Fragment() {
    lateinit var binding: FragmentResultsBinding

    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultsBinding.inflate(layoutInflater)

        sessionViewModel.shouldShowResults.observe(viewLifecycleOwner) { shouldShowResults ->
            if (shouldShowResults) sessionViewModel.finishExercise()
        }

        return binding.root
    }
}