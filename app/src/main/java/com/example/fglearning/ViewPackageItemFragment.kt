package com.example.fglearning

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import com.example.fglearning.databinding.FragmentViewPackageItemBinding
import com.example.fglearning.viewmodel.ExerciseViewModel
import com.example.fglearning.viewmodel.PackageItemsViewModel
import com.example.fglearning.viewmodel.PackagesViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.Int
import kotlin.getValue

class ViewPackageItemFragment : Fragment() {
    lateinit var binding: FragmentViewPackageItemBinding
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val packagesViewModel: PackagesViewModel by activityViewModels()
    private val packageItemsViewModel: PackageItemsViewModel by activityViewModels()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackageItemBinding.inflate(layoutInflater)

        var marked = exerciseViewModel.element.value?.marked

        fun isStringPositionsCorrect(positions: String): Boolean {
            val positions = positions.replace(" ", "")
            val posList = positions.split(",")
            for (item in posList) {
                val item = item.toIntOrNull()
                if (
                    item == null ||
                    item < 1 ||
                    item > binding.contentEditText.text.length
                    ) {
                    return false
                }
            }
            return true
        }

        fun isAccentPositionCorrect(word: String, position: Int): Boolean {
            val vowels = setOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я',
                                'a', 'e', 'i', 'o', 'u', 'y')
            return word[position-1] in vowels
        }

        fun isInputValid(): Boolean {
            return when (sessionViewModel.exerciseType.value) {
                1 -> {
                    binding.firstEditText.text.isNotBlank() &&
                            binding.secondEditText.text.isNotBlank()
                }
                2 -> {
                    val content = binding.contentEditText.text.toString()
                    val position = binding.positionsEditText.text.toString()
                    val positionInt = position.toIntOrNull()

                    content.isNotBlank() &&
                            content.length > 1 &&
                            position.isNotBlank() &&
                            positionInt != null &&
                            positionInt in 1..content.length &&
                            isAccentPositionCorrect(content, positionInt)
                }
                3 -> {
                    val content = binding.contentEditText.text
                    val position = binding.positionsEditText.text.toString()

                    content.isNotBlank() &&
                            content.length > 1 &&
                            position.isNotBlank() &&
                            isStringPositionsCorrect(position)
                }
                else -> false
            }
        }

        fun hasContentChanges(): Boolean {
            val notes = binding.notesEditText.text.toString()
            val difficulty = binding.difficultySpinner.selectedItemPosition
            val currMarked = exerciseViewModel.element.value?.marked
            val currNotes = exerciseViewModel.element.value?.notes
            val currDifficulty = exerciseViewModel.element.value?.difficulty

            if (marked != currMarked ||
                notes != currNotes ||
                difficulty != currDifficulty) {
                return true
            }
            else {
                return when (sessionViewModel.exerciseType.value) {
                    1 -> {
                        val front = binding.firstEditText.text.toString()
                        val back = binding.secondEditText.text.toString()
                        val currFront = exerciseViewModel.flashcard.value?.frontText
                        val currBack = exerciseViewModel.flashcard.value?.backText

                        front != currFront ||
                                back != currBack
                    }

                    2 -> {
                        val content = binding.contentEditText.text
                        val position = binding.positionsEditText.text.toString().toIntOrNull()
                        val currContent = exerciseViewModel.accent.value?.word
                        val currPosition = exerciseViewModel.accent.value?.accentPos

                        content.toString() != currContent ||
                                position != currPosition
                    }

                    3 -> {
                        val content = binding.contentEditText.text
                        val positions = binding.positionsEditText.text.toString()
                        val currContent = exerciseViewModel.insertLetter.value?.word
                        val currPositions = exerciseViewModel.insertLetter.value?.gaps

                        content.toString() != currContent ||
                                positions != currPositions
                    }

                    else -> false
                }
            }
        }

        fun checkSaveButton() {
            if (
                isInputValid() &&
                hasContentChanges()
            ) {
                binding.saveButton.visibility = View.VISIBLE
            }
            else binding.saveButton.visibility = View.INVISIBLE
        }

        val items = listOf("Не выбрано", "Легко", "Неплохо", "Плохо", "Сложно")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        )
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.difficultySpinner.adapter = adapter
        binding.difficultySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
                checkSaveButton()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                checkSaveButton()
            }
        }

        binding.positionsEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButton()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.firstEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButton()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.secondEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButton()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.notesEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButton()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.contentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButton()
                when (sessionViewModel.exerciseType.value) {
                    2 -> {
                        if (binding.contentEditText.text.length > 1) binding.positionsHintText.text = "*Введите позицию ударения от 1 до " + binding.contentEditText.text.length
                        else binding.positionsHintText.text = "*Введите позицию ударения"
                    }
                    3 -> {
                        if (binding.contentEditText.text.length > 1) binding.positionsHintText.text = "*Введите пропуски в виде: 1,2,3 (от 1 до " + binding.contentEditText.text.length + ")"
                        else binding.positionsHintText.text = "*Введите пропуски в виде: 1,2,3"
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.markButton.setOnClickListener {
            if (marked == true) {
                marked = false
                binding.markButton.setImageResource(R.drawable.bookmark)
            }
            else {
                marked = true
                binding.markButton.setImageResource(R.drawable.baseline_bookmark)
            }
            checkSaveButton()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            val exercise = sessionViewModel.exerciseType.value
            if (exercise != null) {
                if (sessionViewModel.adding.value == true) {
                    lifecycleScope.launch {
                        val packetItem = PackageItem(
                            packetId = sessionViewModel.currentPacket.value!!.id,
                            difficulty = binding.difficultySpinner.selectedItemPosition,
                            marked = marked ?: false,
                            notes = binding.notesEditText.text.toString()
                        )
                        when (exercise) {
                            1 -> {
                                val flashcard = Flashcard(
                                    frontText = binding.firstEditText.text.toString(),
                                    backText = binding.secondEditText.text.toString()
                                )
                                exerciseViewModel.addPackageItem(packetItem, flashcard)
                            }
                            2 -> {
                                val accentPos = binding.positionsEditText.text.toString().toIntOrNull() ?: 1
                                val accent = Accent(
                                    word = binding.contentEditText.text.toString(),
                                    accentPos = accentPos - 1
                                )
                                exerciseViewModel.addPackageItem(packetItem, accent)
                            }
                            3 -> {
                                val insertLetter = InsertLetter(
                                    word = binding.contentEditText.text.toString(),
                                    gaps = binding.positionsEditText.text.toString().replace(" ", "")
                                )
                                exerciseViewModel.addPackageItem(packetItem, insertLetter)
                            }
                        }
                    }
                }
                else {
                    sessionViewModel.currentPacket.value?.let { packet ->
                        exerciseViewModel.element.value?.let { element ->
                            lifecycleScope.launch {
                                val updatedElement = element.copy(
                                    packetId = sessionViewModel.currentPacket.value!!.id,
                                    difficulty = binding.difficultySpinner.selectedItemPosition,
                                    marked = marked ?: false,
                                    notes = binding.notesEditText.text.toString()
                                )
                                when (exercise) {
                                    1 -> {
                                        val updatedflashcard = Flashcard(
                                            frontText = binding.firstEditText.text.toString(),
                                            backText = binding.secondEditText.text.toString()
                                        )
                                        exerciseViewModel.addPackageItem(updatedElement, updatedflashcard)
                                    }
                                    2 -> {
                                        val updatedaccent = Accent(
                                            word = binding.contentEditText.text.toString(),
                                            accentPos = binding.positionsEditText.text.toString().toIntOrNull() ?: 0
                                        )
                                        exerciseViewModel.addPackageItem(updatedElement, updatedaccent)
                                    }
                                    3 -> {
                                        val updatedinsertLetter = InsertLetter(
                                            word = binding.contentEditText.text.toString(),
                                            gaps = binding.positionsEditText.text.toString().replace(" ", "")
                                        )
                                        exerciseViewModel.addPackageItem(updatedElement, updatedinsertLetter)
                                    }
                                }
                                exerciseViewModel.setCurrentPacketItem(packet, updatedElement.id)
                            }
                        }
                    }
                }
            }
            findNavController().popBackStack()
        }
        //TODO saveButton

        binding.deleteButton.setOnClickListener {
            exerciseViewModel.element.value?.let { element ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Подтверждение")
                    .setMessage("Вы действительно хотите удалить этот материал?")
                    .setPositiveButton("Да") { dialog, _ ->
                        lifecycleScope.launch {
                            exerciseViewModel.deletePackageItem(element)
                        }
                        dialog.dismiss()
                        when (sessionViewModel.exerciseType.value) {
                            1 -> {
                                val frontText = exerciseViewModel.flashcard.value?.frontText
                                frontText?.let {
                                    if (frontText.length > 15)
                                        Toast.makeText(
                                            requireContext(),
                                            "Карточка \"${frontText.substring(0, 15) + "..."}\" удалена",
                                            Toast.LENGTH_SHORT)
                                            .show()
                                    else
                                        Toast.makeText(
                                            requireContext(),
                                            "Карточка \"${frontText}\" удалена",
                                            Toast.LENGTH_SHORT)
                                            .show()
                                }
                            }
                            2 -> Toast.makeText(
                                requireContext(),
                                "Слово \"${exerciseViewModel.accent.value?.word}\" удалено",
                                Toast.LENGTH_SHORT)
                                .show()
                            3 -> Toast.makeText(
                                requireContext(),
                                "Слово \"${exerciseViewModel.insertLetter.value?.word}\" удалено",
                                Toast.LENGTH_SHORT)
                                .show()
                        }
                        exerciseViewModel.setCurrentPacketItemNull()
                        findNavController().popBackStack()
                    }
                    .setNegativeButton("Отмена") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }

        sessionViewModel.exerciseType.observe(viewLifecycleOwner) { exerciseType ->
            when (exerciseType) {
                1 -> {
                    binding.positions.visibility = View.GONE
                    binding.contentEditText.visibility = View.GONE
                    binding.firstEditText.visibility = View.VISIBLE
                    binding.secondEditText.visibility = View.VISIBLE

                    exerciseViewModel.flashcard.value?.let { flashcard ->
                        binding.firstEditText.setText(flashcard.frontText)
                        binding.secondEditText.setText(flashcard.backText)
                    }
                }
                2 -> {
                    binding.positions.visibility = View.VISIBLE
                    binding.positionsHintText.visibility = View.VISIBLE
                    binding.contentEditText.visibility = View.VISIBLE
                    binding.firstEditText.visibility = View.GONE
                    binding.secondEditText.visibility = View.GONE

                    binding.positionsHeaderText.text = "Позиция:"
                    binding.positionsEditText.hint = "число"
                    binding.positionsEditText.maxEms = 2
                    binding.positionsEditText.inputType = InputType.TYPE_NUMBER_VARIATION_NORMAL //"number"
                    binding.positionsEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"))

                    exerciseViewModel.accent.value?.let { accent ->
                        if (binding.contentEditText.text.length > 1) binding.positionsHintText.text = "*Введите позицию ударения от 1 до " + binding.contentEditText.text.length
                        else binding.positionsHintText.text = "*Введите позицию ударения"
                        binding.contentEditText.setText(accent.word)
                        binding.positionsEditText.setText(accent.accentPos.toString())
                    }
                }
                3 -> {
                    binding.positions.visibility = View.VISIBLE
                    binding.positionsHintText.visibility = View.VISIBLE
                    binding.contentEditText.visibility = View.VISIBLE
                    binding.firstEditText.visibility = View.GONE
                    binding.secondEditText.visibility = View.GONE

                    binding.positionsHeaderText.text = "Пропуски:"
                    binding.positionsEditText.hint = "1,2,3"
                    binding.positionsEditText.maxEms = 20
                    binding.positionsEditText.inputType = InputType.TYPE_TEXT_VARIATION_NORMAL //"number + ,"
                    binding.positionsEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789,"))

                    exerciseViewModel.insertLetter.value?.let { insertLetter ->
                        if (binding.contentEditText.text.length > 1) binding.positionsHintText.text = "*Введите пропуски в виде: 1,2,3 (от 1 до " + binding.contentEditText.text.length + ")"
                        else binding.positionsHintText.text = "*Введите пропуски в виде: 1,2,3"
                        binding.contentEditText.setText(insertLetter.word)
                        binding.positionsEditText.setText(insertLetter.gaps)
                    }
                }
            }
        }

        sessionViewModel.adding.observe(viewLifecycleOwner) { isAdding ->
            if (isAdding) {
                exerciseViewModel.setCurrentPacketItemNull()
                binding.deleteButton.visibility = View.GONE
                binding.markButton.visibility = View.GONE
                binding.stats.visibility = View.GONE
                binding.contentEditText.setText("")
                binding.firstEditText.setText("")
                binding.secondEditText.setText("")
                binding.difficultySpinner.setSelection(0)
                //binding.positionsHintText.visibility = View.VISIBLE
                binding.positionsEditText.setText("")
            } else {
                binding.deleteButton.visibility = View.VISIBLE
                binding.markButton.visibility = View.VISIBLE
                binding.stats.visibility = View.VISIBLE

                exerciseViewModel.element.value?.let { element ->
                    binding.difficultySpinner.setSelection(element.difficulty)
                    var format: String = "никогда"
                    if (element.lastViewTimestamp != -1L) {
                        val date = Date(element.lastViewTimestamp * (60 * 1000))  //минуты
                        format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(date)
                    }
                    binding.lastViewText.text = format
                    binding.notesEditText.setText(element.notes)
                    binding.lastCorrectAnsCountText.text = element.lastCountCorrect.toString()
                    binding.lastIncorrectAnsCountText.text = element.lastCountIncorrect.toString()
                    binding.totalCorrectAnsCountText.text = element.totalCountCorrect.toString()
                    binding.totalIncorrectAnsCountText.text = element.totalCountIncorrect.toString()
                    if (exerciseViewModel.element.value?.marked == false) binding.markButton.setImageResource(R.drawable.bookmark)
                    else binding.markButton.setImageResource(R.drawable.baseline_bookmark)
                }

                /*exerciseViewModel.element.value?.let { element ->
                    binding.contentEditText.setText("")
                    binding.firstEditText.setText("")
                    binding.secondEditText.setText("")
                    binding.difficultySpinner.setSelection(0)
                    binding.gapsHintText.visibility = View.GONE
                    binding.gapsEditText.setText("")
                }*/
            }
        }

        return binding.root
    }
}