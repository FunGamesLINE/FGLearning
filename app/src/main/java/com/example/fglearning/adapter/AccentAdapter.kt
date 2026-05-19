package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
            binding.letterLayout.setBackgroundResource(R.drawable.shape_rounded)

            updateBackground(position, letter)

            binding.root.setOnClickListener {
                selectedPosition = position
                onItemClick?.invoke(letter, position)
            }
        }

        private fun updateBackground(position: Int, letter: Char) {
            val drawable = binding.letterLayout.background
            val context = binding.root.context

            if (needToShowAnswer) {
                if (selectedPosition == position) {
                    if (correctPos == position) {
                        if (letter in vowels) {
                            drawable.setTint(ContextCompat.getColor(context, R.color.green))
                        }
                        else drawable?.setTint(ContextCompat.getColor(context, R.color.green))
                    }
                    else {
                        if (letter in vowels) drawable?.setTint(ContextCompat.getColor(context, R.color.red))
                        else drawable?.setTint(ContextCompat.getColor(context, R.color.red))
                    }
                }
                else {
                    if (correctPos == position) {
                        if (letter in vowels) drawable?.setTint(ContextCompat.getColor(context, R.color.green))
                        else drawable?.setTint(ContextCompat.getColor(context, R.color.green))
                    }
                    else {
                        if (letter in vowels) drawable?.setTint(ContextCompat.getColor(context, R.color.light_gray))
                        else drawable?.setTint(ContextCompat.getColor(context, R.color.gray))
                    }
                }
            }
            else {
                if (letter in vowels) drawable?.setTint(ContextCompat.getColor(context, R.color.light_gray))
                else drawable?.setTint(ContextCompat.getColor(context, R.color.gray))
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