package com.example.fglearning.adapter

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.R
import com.example.fglearning.adapter.PackageAdapter.PackageViewHolder
import com.example.fglearning.databinding.ItemInsertLetterBinding
import com.example.fglearning.databinding.ItemLetterBinding
import com.example.fglearning.databinding.ItemPackageBinding
import kotlin.text.trim

class InsertLettersAdapter(
    private val recyclerView: RecyclerView?,
    val letters: MutableList<Char>,
    private var gaps: List<Int>,
): RecyclerView.Adapter<InsertLettersAdapter.InsertLettersViewHolder>() {
    private var needToShowAnswer: Boolean = false
    private var gapsListIndex: Int = -1
    private var needToDeleteSymbol: Boolean = false
    private var editTextIsUpdating = false
    private val userInput = mutableMapOf<Int, Char>()

    private fun shouldFocus(position: Int): Boolean {
        if (gapsListIndex != -1) {
            return position == gaps[gapsListIndex]
        }
        return false
    }

    private fun requestFocusToPosition(position: Int) {
        recyclerView?.post {
            val holder = recyclerView.findViewHolderForAdapterPosition(position) as? InsertLettersViewHolder
            holder?.binding?.letterEditText?.requestFocus()
        }
    }

    private fun requestFocusToNext() {
        if (gapsListIndex < (gaps.size - 1)) {
            gapsListIndex++
            requestFocusToPosition(gaps[gapsListIndex])
        }
    }

    private fun requestFocusToPrevious() {
        if (gapsListIndex > 0) {
            gapsListIndex--
            needToDeleteSymbol = true
            requestFocusToPosition(gaps[gapsListIndex])
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InsertLettersViewHolder {
        val binding = ItemInsertLetterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InsertLettersViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: InsertLettersViewHolder,
        position: Int
    ) {
        holder.bind(letters[position], position)
    }

    override fun getItemCount(): Int = letters.size

    inner class InsertLettersViewHolder (val binding: ItemInsertLetterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(letter: Char, position: Int) {
            binding.letter = letter
            binding.letterLayout.setBackgroundResource(R.drawable.shape_rounded)

            val drawable = binding.letterLayout.background
            val context = binding.root.context

            if (needToShowAnswer) {
                if (position in gaps) {
                    Log.d("MyAdapter", binding.letterEditText.text.toString() + " " + letters[position].toString() + position)
                    if (userInput[position]?.uppercase() == letters[position].toString().uppercase()) drawable.setTint(ContextCompat.getColor(context, R.color.green))
                    else drawable.setTint(ContextCompat.getColor(context, R.color.red))
                }
                else drawable.setTint(ContextCompat.getColor(context, R.color.gray))
                binding.letterEditText.setText(letter.toString().uppercase())
                binding.letterEditText.isEnabled = false
            }
            else {
                if (position in gaps) {
                    drawable.setTint(ContextCompat.getColor(context, R.color.light_gray))
                    binding.letterEditText.isEnabled = true
                    binding.letterEditText.setText("")
                }
                else {
                    drawable.setTint(ContextCompat.getColor(context, R.color.gray))
                    binding.letterEditText.setText(letter.toString().uppercase())
                    binding.letterEditText.isEnabled = false
                }
            }

            if (shouldFocus(position)) {
                binding.letterEditText.post {
                    if (needToDeleteSymbol) {
                        binding.letterEditText.setText("")
                        needToDeleteSymbol = false
                    }
                    binding.letterEditText.requestFocus()
                }
            }

            binding.root.setOnClickListener {
                binding.letterEditText.post {
                    binding.letterEditText.requestFocus()
                }
            }

            binding.letterEditText.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    gapsListIndex = gaps.indexOf(position)
                }
            }

            binding.letterEditText.addTextChangedListener(object : TextWatcher {
                private var previousText = ""

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    previousText = s?.toString() ?: ""
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
                override fun afterTextChanged(s: Editable?) {
                    if (editTextIsUpdating) return
                    editTextIsUpdating = true
                    val text = s?.toString() ?: ""

                    if (text.isEmpty()) {
                        if (previousText.isNotEmpty()) {
                            userInput.remove(position)
                            requestFocusToPrevious()
                        }
                    } else {
                        val lastChar = text.takeLast(1).uppercase()
                        s?.replace(0, text.length, lastChar)
                        userInput[position] = lastChar[0]
                        binding.letterEditText.post { requestFocusToNext() }
                    }
                    editTextIsUpdating = false
                }
            })
        }
    }

    fun setItems(newItems: List<Char>, newGaps: List<Int>) {
        userInput.clear()
        letters.clear()
        letters.addAll(newItems)
        gaps = newGaps
        gapsListIndex = -1
        needToShowAnswer = false
        needToDeleteSymbol = false
        editTextIsUpdating = false
        notifyDataSetChanged()
    }

    fun showAnswerAndGetUserAnswers(): Map<Int, Char> {
        needToShowAnswer = true
        notifyDataSetChanged()
        return userInput.toMap()
    }
}