package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.adapter.PackageAdapter.PackageViewHolder
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.databinding.ItemLetterBinding
import com.example.fglearning.databinding.ItemPackageBinding

class WordAccentAdapter(
    val letters: MutableList<Char>,
    //val correct: Int
) : RecyclerView.Adapter<WordAccentAdapter.WordAccentViewHolder>() {
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
        holder.bind(letters[position])
    }

    override fun getItemCount(): Int = letters.size

    class WordAccentViewHolder (private val binding: ItemLetterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(letter: Char) {
            binding.letter = letter
        }
    }

    fun setItems(newItems: List<Char>){
        letters.clear()
        letters.addAll(newItems)
        notifyDataSetChanged()
    }
}