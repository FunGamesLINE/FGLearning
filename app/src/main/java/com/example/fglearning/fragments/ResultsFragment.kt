package com.example.fglearning.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.fglearning.R
import com.example.fglearning.databinding.FragmentResultsBinding
import com.example.fglearning.viewmodel.ExerciseViewModel
import com.example.fglearning.viewmodel.PackageItemsViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
            if (shouldShowResults) sessionViewModel.shownResults()
        }

        fun progressBarAnimation() {
            lifecycleScope.launch {
                while (binding.progressBar.progress != ) {
                    binding.progressBar.progress += 1
                    delay(20)
                }
            }
        }

        lifecycleScope.launch {
            sessionViewModel.currentPacket.value?.let { currentPacket ->
                //TODO visibilities
                when (sessionViewModel.exerciseType.value) {
                    1 -> {
                        binding.memorabilityLayout.visibility = View.VISIBLE
                        binding.correctAndIncorrectLayout.visibility = View.GONE
                    }
                    2,3 -> {
                        binding.memorabilityLayout.visibility = View.GONE
                        binding.correctAndIncorrectLayout.visibility = View.VISIBLE
                    }
                }

                //TODO progress
                binding.progressCorrect.visibility = View.VISIBLE
                binding.progressIncorrect.visibility = View.VISIBLE
                binding.progressHardDifficultyLayout.visibility = View.VISIBLE
                binding.progressNotBadDifficultyLayout.visibility = View.VISIBLE
                binding.progressBadDifficultyLayout.visibility = View.VISIBLE
                binding.progressEasyDifficultyLayout.visibility = View.VISIBLE
                binding.progressNotSelectedDifficultyLayout.visibility = View.VISIBLE

                //TODO arrow color (red/green)

                binding.countHardDifficulty.text = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 4).toString()
                binding.countBadDifficulty.text = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 3).toString()
                binding.countNotBadDifficulty.text = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 2).toString()
                binding.countEasyDifficulty.text = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 1).toString()
                binding.countNotSelectedDifficulty.text = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 0).toString()

                exerciseViewModel.getOldExerciseResults()?.let { oldExerciseResults ->
                    binding.oldCountHardDifficulty.text = oldExerciseResults.countHard.toString()
                    binding.oldCountBadDifficulty.text = oldExerciseResults.countBad.toString()
                    binding.oldCountNotBadDifficulty.text = oldExerciseResults.countNotBad.toString()
                    binding.oldCountEasyDifficulty.text = oldExerciseResults.countEasy.toString()
                    binding.oldCountNotSelectedDifficulty.text = oldExerciseResults.countNotSelected.toString()
                }

                binding.countCorrect.text =
                binding.countIncorrect.text =
                binding.oldCountCorrect.text =
                binding.oldCountIncorrect.text =

                binding.countDoneText.text = exerciseViewModel.doneCount.toString()
                val countTotal = (exerciseViewModel.doneCount.value ?: 0) + exerciseViewModel.countLeftItems()
                binding.countTotalText.text = countTotal.toString()

                binding.recordCountCorrect.text =

            }
        }

        binding.againButton.setOnClickListener {
            sessionViewModel.startExercise()
            findNavController().popBackStack()
        }

        binding.finishButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.resetRecordButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Подтверждение")
                .setMessage("Вы действительно хотите cбросить рекорд?")
                .setPositiveButton("Да") { dialog, _ ->
                    lifecycleScope.launch {
                        sessionViewModel.resetCurrentPackageRecord()
                    }
                    dialog.dismiss()
                    Toast.makeText(
                        requireContext(),
                        "Рекорд сброшен!",
                        Toast.LENGTH_SHORT)
                        .show()
                    binding.recordCountCorrect.text = "0"
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        return binding.root
    }
}