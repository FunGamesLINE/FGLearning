package com.example.fglearning.fragments.exercises

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fglearning.ExerciseData
import com.example.fglearning.R
import com.example.fglearning.adapter.AccentAdapter
import com.example.fglearning.database.entity.PackageItem
import com.example.fglearning.databinding.FragmentRunFlashcardsBinding
import com.example.fglearning.viewmodel.ExerciseViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class RunFlashcardsFragment : Fragment() {
    lateinit var binding: FragmentRunFlashcardsBinding

    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    private var delayJob: Job? = null

    //private var needToShowNextMaterial: Boolean = false
    private var isCorrect: Boolean = false
    private var isNotesEditTextChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunFlashcardsBinding.inflate(layoutInflater)

        fun nextMaterial(newCurrentPacketItemDifficulty: Int) {
            delayJob?.cancel()

            delayJob = lifecycleScope.launch {
                binding.notesLayout.visibility = View.GONE
                binding.setDifficultyButtonsLayout.visibility = View.GONE
                binding.answerAndSeparatorLayout.visibility = View.GONE
                isNotesEditTextChanged = false

                exerciseViewModel.currentPackageItemWithData.value?.packageItem?.let { currentPackageItem ->
                    val packetItem = PackageItem(
                        //marked = updatedPacketItem.marked,
                        packetId = currentPackageItem.packetId,
                        difficulty = newCurrentPacketItemDifficulty,
                        marked = currentPackageItem.marked,
                        notes = binding.notesEditText.text.toString()
                    )
                    sessionViewModel.exerciseType.value?.let { exerciseType ->
                        exerciseViewModel.updateCurrentPackageItem(
                            packetItem,
                            exerciseType,
                            isCorrect
                        )
                    }
                }
                if (!exerciseViewModel.setRandomPacketItem()) {
                    sessionViewModel.finishExercise()
                    delayJob?.cancel()
                }

                delayJob = null
            }
        }

        fun showAnswer() {
            binding.showAnswerButton.visibility = View.GONE
            binding.notesButtonsLayout.visibility = View.GONE
            binding.notesLayout.visibility = View.VISIBLE
            binding.answerAndSeparatorLayout.visibility = View.VISIBLE
            binding.setDifficultyButtonsLayout.visibility = View.VISIBLE

            binding.notesEditText.setText(exerciseViewModel.currentPackageItemWithData.value?.packageItem?.notes)

            exerciseViewModel.currentPackageItemWithData.value?.let { packageItemWithData ->
                when (val content = packageItemWithData.content) {
                    is ExerciseData.Flashcard -> {
                        binding.secondText.text = content.backText
                    }

                    else -> {}
                }
            }
        }

        sessionViewModel.shouldFinishExercise.observe(viewLifecycleOwner) { shouldFinishExercise ->
            if (shouldFinishExercise) findNavController().popBackStack()
        }

        exerciseViewModel.currentPackageItemWithData.observe(viewLifecycleOwner) { packageItemWithData ->
            packageItemWithData?.let {
                when (val content = packageItemWithData.content) {
                    is ExerciseData.Flashcard -> {
                        binding.notesLayout.visibility = View.GONE
                        binding.answerAndSeparatorLayout.visibility = View.GONE
                        binding.setDifficultyButtonsLayout.visibility = View.GONE
                        binding.showAnswerButton.visibility = View.VISIBLE
                        binding.firstText.text = content.frontText
                    }

                    else -> {}
                }
                binding.countLeftText.text = exerciseViewModel.countLeftItems().toString()
                sessionViewModel.currentPacket.value?.let { currentPacket ->
                    lifecycleScope.launch {
                        binding.countHardDifficultyText.text =
                            exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 4)
                                .toString()
                        binding.countBadDifficultyText.text =
                            exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 3)
                                .toString()
                        binding.countNotBadDifficultyText.text =
                            exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 2)
                                .toString()
                        binding.countEasyDifficultyText.text =
                            exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 1)
                                .toString()
                        binding.countNotSelectedDifficultyText.text =
                            exerciseViewModel.countByPacketAndDifficulty(currentPacket.id, 0)
                                .toString()
                    }
                }
            }
        }

        binding.notesEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && !isNotesEditTextChanged) {
                binding.setDifficultyButtonsLayout.visibility = View.VISIBLE
                binding.notesButtonsLayout.visibility = View.GONE
                isNotesEditTextChanged = false
            }
        }
        binding.notesEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != exerciseViewModel.currentPackageItemWithData.value?.packageItem?.notes) {
                    binding.setDifficultyButtonsLayout.visibility = View.GONE
                    binding.notesButtonsLayout.visibility = View.VISIBLE
                    isNotesEditTextChanged = true
                } else {
                    binding.setDifficultyButtonsLayout.visibility = View.VISIBLE
                    binding.notesButtonsLayout.visibility = View.GONE
                    isNotesEditTextChanged = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.saveButton.setOnClickListener {
            binding.setDifficultyButtonsLayout.visibility = View.VISIBLE
            binding.notesButtonsLayout.visibility = View.GONE
            isNotesEditTextChanged = true
        }

        binding.cancelButton.setOnClickListener {
            binding.notesEditText.setText(exerciseViewModel.currentPackageItemWithData.value?.packageItem?.notes)
            binding.setDifficultyButtonsLayout.visibility = View.VISIBLE
            binding.notesButtonsLayout.visibility = View.GONE
            isNotesEditTextChanged = false
        }

        binding.setDifficultyHardButton.setOnClickListener {
            nextMaterial(4)
        }
        binding.setDifficultyBadButton.setOnClickListener {
            nextMaterial(3)
        }
        binding.setDifficultyNotBadButton.setOnClickListener {
            nextMaterial(2)
        }
        binding.setDifficultyEasyButton.setOnClickListener {
            nextMaterial(1)
        }

        binding.showAnswerButton.setOnClickListener {
            showAnswer()
        }

        return binding.root
    }
}