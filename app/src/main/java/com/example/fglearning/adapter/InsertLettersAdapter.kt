package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.adapter.PackageAdapter.PackageViewHolder
import com.example.fglearning.databinding.ItemInsertLetterBinding
import com.example.fglearning.databinding.ItemLetterBinding
import com.example.fglearning.databinding.ItemPackageBinding

class InsertLettersAdapter(
    val letters: MutableList<Char>,
    //val correct: String
): RecyclerView.Adapter<InsertLettersAdapter.InsertLettersViewHolder>() {
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
        holder.bind(letters[position])
    }

    override fun getItemCount(): Int = letters.size

    class InsertLettersViewHolder (private val binding: ItemInsertLetterBinding) : RecyclerView.ViewHolder(binding.root) {
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