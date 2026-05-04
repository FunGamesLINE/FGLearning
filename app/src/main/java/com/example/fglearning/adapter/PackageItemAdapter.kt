package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.databinding.ItemPackageElementBinding
import com.example.fglearning.database.entity.PackageItem

class PackageItemAdapter (
    val items: MutableList<PackageItem>
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

    fun addPackage(item: PackageItem) {
        items.add(item)
        notifyDataSetChanged()
    }

    class PackageItemViewHolder(private val binding: ItemPackageElementBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PackageItem) {
            binding.packageItem = item
        }
    }

    fun setItems(newItems: List<PackageItem>){
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}