package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.text.toUpperCase
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.PackageItemWithData
import com.example.fglearning.R
import com.example.fglearning.databinding.ItemLetterBinding

class AccentAdapter(
    val letters: MutableList<Char>,
    private var correctPos: Int?,
    val onItemClick: ((Char, Int) -> Unit)? = null
) : RecyclerView.Adapter<AccentAdapter.WordAccentViewHolder>() {
    private val vowels = setOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я',
                                'a', 'e', 'i', 'o', 'u', 'y')

    private var needToShowAnswer: Boolean = false
    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WordAccentViewHolder {
        val binding = ItemLetterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WordAccentViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WordAccentViewHolder,
        position: Int
    ) {
        holder.bind(letters[position], position)
    }

    override fun getItemCount(): Int = letters.size

    inner class WordAccentViewHolder (private val binding: ItemLetterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(letter: Char, position: Int) {
            binding.letter = letter
            binding.letterText.text = letter.toString().uppercase()

            if (needToShowAnswer) {
                if (selectedPosition == position) {
                    if (correctPos == position) {
                        if (letter in vowels) binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_right)
                        else binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_right)
                    }
                    else {
                        if (letter in vowels) binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_wrong)
                        else binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_wrong)
                    }
                }
                else {
                    if (correctPos == position) {
                        if (letter in vowels) binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_right)
                        else binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_right)
                    }
                    else {
                        if (letter in vowels) binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_active)
                        else binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_inactive)
                    }
                }
            }
            else {
                if (letter in vowels) binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_active)
                else binding.letterLayout.setBackgroundResource(R.drawable.shape_item_letter_inactive)
            }

            binding.root.setOnClickListener {
                selectedPosition = position
                onItemClick?.invoke(letter, position)
            }
        }
    }

    fun setItems(newItems: List<Char>, _correctPos: Int) {
        letters.clear()
        letters.addAll(newItems)
        correctPos = _correctPos
        needToShowAnswer = false
        notifyDataSetChanged()
    }

    fun showAnswer() {
        needToShowAnswer = true
        notifyDataSetChanged()
    }
//    fun hideAnswer() {
//        needToShowAnswer = false
//    }

}