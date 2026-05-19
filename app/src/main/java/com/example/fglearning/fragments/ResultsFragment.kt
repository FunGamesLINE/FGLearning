package com.example.fglearning.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.fglearning.R
import com.example.fglearning.database.entity.Package
import com.example.fglearning.databinding.FragmentResultsBinding
import com.example.fglearning.viewmodel.ExerciseViewModel
import com.example.fglearning.viewmodel.PackageItemsViewModel
import com.example.fglearning.viewmodel.PackagesViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class ResultsFragment : Fragment() {
    lateinit var binding: FragmentResultsBinding

    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()
    private val packagesViewModel: PackagesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultsBinding.inflate(layoutInflater)

        sessionViewModel.shouldShowResults.observe(viewLifecycleOwner) { shouldShowResults ->
            if (shouldShowResults) {
                sessionViewModel.shownResults()
            }
        }

        fun progressBarAnimation() {
            binding.progressBar.progress = 0
            lifecycleScope.launch {
                val countDone = exerciseViewModel.doneCount.value ?: 0
                val countTotal = countDone + exerciseViewModel.countLeftItems()
                while (binding.progressBar.progress < (countDone * 100 / countTotal)) {
                    binding.progressBar.progress += 1
                    delay(10)
                }
            }
        }

        fun showResults() {
            lifecycleScope.launch {
                progressBarAnimation()
                sessionViewModel.currentPacket.value?.let { currentPacket ->
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

                    val countHardDifficulty = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 4)
                    val countBadDifficulty = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 3)
                    val countNotBadDifficulty = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 2)
                    val countEasyDifficulty = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 1)
                    val countNotSelectedDifficulty = exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 0)
                    val countCorrect = exerciseViewModel.getTotalCorrectCount()
                    val countIncorrect = exerciseViewModel.getTotalIncorrectCount()
                    binding.countHardDifficulty.text = countHardDifficulty.toString()
                    binding.countBadDifficulty.text = countBadDifficulty.toString()
                    binding.countNotBadDifficulty.text = countNotBadDifficulty.toString()
                    binding.countEasyDifficulty.text = countEasyDifficulty.toString()
                    binding.countNotSelectedDifficulty.text = countNotSelectedDifficulty.toString()
                    binding.countCorrect.text = countCorrect.toString()
                    binding.countIncorrect.text = countIncorrect.toString()
                    val oldExerciseResults = exerciseViewModel.getOldExerciseResults()
                    if (oldExerciseResults != null) {
                        if (oldExerciseResults.countHard != countHardDifficulty) {
                            binding.progressHardDifficultyLayout.visibility = View.VISIBLE
                            if (oldExerciseResults.countHard < countHardDifficulty) binding.arrowHardDifficulty.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                            else binding.arrowHardDifficulty.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                        }
                        else binding.progressHardDifficultyLayout.visibility = View.GONE

                        if (oldExerciseResults.countBad != countBadDifficulty) {
                            binding.progressBadDifficultyLayout.visibility = View.VISIBLE
                            binding.arrowBadDifficulty.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        }
                        else binding.progressBadDifficultyLayout.visibility = View.GONE

                        if (oldExerciseResults.countNotBad != countNotBadDifficulty) {
                            binding.progressNotBadDifficultyLayout.visibility = View.VISIBLE
                            binding.arrowNotBadDifficulty.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
                        }
                        else binding.progressNotBadDifficultyLayout.visibility = View.GONE

                        if (oldExerciseResults.countEasy != countEasyDifficulty) {
                            binding.progressEasyDifficultyLayout.visibility = View.VISIBLE
                            if (oldExerciseResults.countEasy > countEasyDifficulty) binding.arrowEasyDifficulty.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                            else binding.arrowEasyDifficulty.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                        }
                        else binding.progressEasyDifficultyLayout.visibility = View.GONE

                        if (oldExerciseResults.totalCorrect != countCorrect) {
                            binding.progressCorrectLayout.visibility = View.VISIBLE
                            if (oldExerciseResults.totalCorrect > countCorrect) binding.arrowCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                            else binding.arrowCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                        }
                        else binding.progressCorrectLayout.visibility = View.GONE

                        if (oldExerciseResults.totalIncorrect != countIncorrect) {
                            binding.progressIncorrectLayout.visibility = View.VISIBLE
                            if (oldExerciseResults.totalIncorrect < countIncorrect) binding.arrowIncorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                            else binding.arrowIncorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                        }
                        else binding.progressIncorrectLayout.visibility = View.GONE

                        binding.progressNotBadDifficultyLayout.visibility = View.VISIBLE
                        binding.progressBadDifficultyLayout.visibility = View.VISIBLE
                        binding.progressEasyDifficultyLayout.visibility = View.VISIBLE
                        binding.progressNotSelectedDifficultyLayout.visibility = View.VISIBLE
                        binding.progressCorrectLayout.visibility = View.VISIBLE
                        binding.progressIncorrectLayout.visibility = View.VISIBLE

                        binding.oldCountHardDifficulty.text = oldExerciseResults.countHard.toString()
                        binding.oldCountBadDifficulty.text = oldExerciseResults.countBad.toString()
                        binding.oldCountNotBadDifficulty.text = oldExerciseResults.countNotBad.toString()
                        binding.oldCountEasyDifficulty.text = oldExerciseResults.countEasy.toString()
                        binding.oldCountNotSelectedDifficulty.text = oldExerciseResults.countNotSelected.toString()
                        binding.oldCountCorrect.text = oldExerciseResults.totalCorrect.toString()
                        binding.oldCountIncorrect.text = oldExerciseResults.totalIncorrect.toString()
                    }
                    else {
                        binding.oldCountHardDifficulty.text = "0"
                        binding.oldCountBadDifficulty.text = "0"
                        binding.oldCountNotBadDifficulty.text = "0"
                        binding.oldCountEasyDifficulty.text = "0"
                        binding.oldCountNotSelectedDifficulty.text = "0"
                        binding.oldCountCorrect.text = "0"
                        binding.oldCountIncorrect.text = "0"
                    }

                    binding.countDoneText.text = exerciseViewModel.doneCount.value.toString()
                    val countTotal = (exerciseViewModel.doneCount.value ?: 0) + exerciseViewModel.countLeftItems()
                    binding.countTotalText.text = countTotal.toString()

                    val recordCountCorrect = sessionViewModel.currentPacket.value?.recordCountCorrect
                    var newRecordCountCorrect = recordCountCorrect
                    recordCountCorrect?.let {
                        val totalCorrectCount = exerciseViewModel.getTotalCorrectCount()
                        if (recordCountCorrect < totalCorrectCount) {
                            binding.recordCountCorrect.text = recordCountCorrect.toString()
                            binding.recordHeader.text = "Старый рекорд: "
                            binding.recordCountCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                            binding.recordHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                            newRecordCountCorrect = totalCorrectCount
                        }
                        else if (recordCountCorrect == totalCorrectCount) {
                            binding.recordCountCorrect.text = recordCountCorrect.toString()
                            binding.recordHeader.text = "Рекорд: "
                            binding.recordCountCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                            binding.recordHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.orange))
                        }
                        else {
                            binding.recordCountCorrect.text = recordCountCorrect.toString()
                            binding.recordHeader.text = "Рекорд: "
                            binding.recordCountCorrect.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                            binding.recordHeader.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
                        }
                    }

                    sessionViewModel.currentPacket.value?.let { packet ->
                        lifecycleScope.launch {
                            val updatedPacket = if (newRecordCountCorrect != null) packet.copy(lastCountCorrect = countCorrect, lastCountIncorrect = countIncorrect, recordCountCorrect = newRecordCountCorrect)
                                else packet.copy(lastCountCorrect = countCorrect, lastCountIncorrect = countIncorrect)
                            packagesViewModel.addPackage(updatedPacket)
                            sessionViewModel.setCurrentPacket(updatedPacket)
                        }
                    }
                }
            }
        }

        showResults()

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