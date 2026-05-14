package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.ExerciseData
import com.example.fglearning.PackageItemWithData
import com.example.fglearning.R
import com.example.fglearning.databinding.ItemPackageElementBinding
import com.example.fglearning.database.entity.PackageItem

class PackageItemAdapter (
    val items: MutableList<PackageItemWithData>,
    private val exerciseType: Int?,
    val onItemClick: ((PackageItemWithData) -> Unit)? = null,
    val onMarkClick: ((PackageItemWithData) -> Unit)? = null,
) : RecyclerView.Adapter<PackageItemAdapter.PackageItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PackageItemViewHolder {
        val binding = ItemPackageElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PackageItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PackageItemViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addPackage(item: PackageItemWithData) {
        items.add(item)
        notifyDataSetChanged()
    }

    inner class PackageItemViewHolder(private val binding: ItemPackageElementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PackageItemWithData) {
            binding.packageItemWithData = item

            if (item.packageItem.marked) binding.markButton.setImageResource(R.drawable.baseline_bookmark)
            else binding.markButton.setImageResource(R.drawable.bookmark)

            binding.itemContentText.visibility = View.GONE
            binding.TwoTextsAndSeparator.visibility = View.GONE

            when (exerciseType) {
                1 -> {
                    (item.content as? ExerciseData.Flashcard)?.let { flashcard ->
                        binding.TwoTextsAndSeparator.visibility = View.VISIBLE
                        if (flashcard.frontText.length > 40) binding.firstText.text = flashcard.frontText.substring(0, 36) + " ..."
                        else binding.firstText.text = flashcard.frontText

                        if (flashcard.backText.length > 40) binding.secondText.text = flashcard.backText.substring(0, 36) + " ..."
                        else binding.secondText.text = flashcard.backText
                    }
                }
                2 -> {
                    (item.content as? ExerciseData.Accent)?.let { accent ->
                        binding.itemContentText.visibility = View.VISIBLE

                        if (accent.word.length > 20) binding.itemContentText.text = accent.word.substring(0, 16) + " ..."
                        else binding.itemContentText.text = accent.word
                    }
                }
                3 -> {
                    (item.content as? ExerciseData.InsertLetter)?.let { insertLetter ->
                        binding.itemContentText.visibility = View.VISIBLE

                        if (insertLetter.word.length > 20) binding.itemContentText.text = insertLetter.word.substring(0, 16) + " ..."
                        else binding.itemContentText.text = insertLetter.word
                    }
                }
            }

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }

            binding.markButton.setOnClickListener {
                onMarkClick?.invoke(item)
            }
        }
    }

    fun setItems(newItems: List<PackageItemWithData>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}