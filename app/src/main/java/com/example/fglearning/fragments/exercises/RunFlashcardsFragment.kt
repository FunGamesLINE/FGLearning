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
    private val vowels = setOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я',
        'a', 'e', 'i', 'o', 'u', 'y')

    private var delayJob: Job? = null
    private var needToShowNextMaterial: Boolean = false
    private var isCorrect: Boolean = false
    private var wasTextFocused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRunFlashcardsBinding.inflate(layoutInflater)

        /*
        fun showAnswer() {
            binding.continueButtonLayout?.visibility = View.GONE
            binding.notesButtonsLayout?.visibility = View.GONE
            binding.notesLayout?.visibility = View.VISIBLE
            binding.indicatorsLayout?.visibility = View.VISIBLE

            binding.notesEditText?.setText(exerciseViewModel.currentPackageItemWithData.value?.packageItem?.notes)
            var currentScores = exerciseViewModel.getCurrentItemScores()
            if (isCorrect) currentScores?.let { currentScores++ }
            else {
                currentScores?.let {
                    if (it > 0) currentScores--
                    currentScores = 0
                }
            }
            delayAndNextMaterial()
        }

        sessionViewModel.shouldFinishExercise.observe(viewLifecycleOwner) { shouldFinishExercise ->
            if(shouldFinishExercise) findNavController().popBackStack()
        }

        exerciseViewModel.currentPackageItemWithData.observe(viewLifecycleOwner) { packageItemWithData ->
            packageItemWithData?.let {
                when (val content = packageItemWithData.content) {
                    is ExerciseData.Accent -> {
                        adapter.setItems(content.word.toCharArray().toList(), content.accentPos)
                        binding.notesLayout?.visibility = View.GONE
                        binding.indicatorsLayout?.visibility = View.GONE
                    }
                    else -> {}
                }
            }
        }

        val countTotal = (exerciseViewModel.doneCount.value ?: 0) + exerciseViewModel.countLeftItems()
        binding.countTotalText?.text = countTotal.toString()

        exerciseViewModel.doneCount.observe(viewLifecycleOwner) { countDone ->
            binding.countDoneText?.text = countDone.toString()
        }

        binding.notesEditText?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                if (!needToShowNextMaterial) {
                    stopNextMaterial()
                    binding.continueButtonLayout?.visibility = View.VISIBLE
                    binding.notesButtonsLayout?.visibility = View.GONE
                    wasTextFocused = true
                }
            }
        }
        binding.notesEditText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (wasTextFocused) {
                    if (s.toString() != exerciseViewModel.currentPackageItemWithData.value?.packageItem?.notes) {
                        binding.continueButtonLayout?.visibility = View.GONE
                        binding.notesButtonsLayout?.visibility = View.VISIBLE
                    } else {
                        binding.continueButtonLayout?.visibility = View.VISIBLE
                        binding.notesButtonsLayout?.visibility = View.GONE
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.saveButton?.setOnClickListener {
            delayAndNextMaterial(0)
        }

        binding.continueButton?.setOnClickListener {
            delayAndNextMaterial(0)
        }

        binding.cancelButton?.setOnClickListener {
            binding.notesEditText?.setText(exerciseViewModel.currentPackageItemWithData.value?.packageItem?.notes)
            delayAndNextMaterial(0)
        }

         */

        return binding.root
    }
}