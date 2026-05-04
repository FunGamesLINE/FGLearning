package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.databinding.ItemPackageBinding
import com.example.fglearning.database.entity.Package

class PackageAdapter(
    val packages: MutableList<Package>
) : RecyclerView.Adapter<PackageAdapter.PackageViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PackageViewHolder {
        val binding = ItemPackageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PackageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PackageViewHolder,
        position: Int
    ) {
        holder.bind(packages[position])
    }

    override fun getItemCount(): Int = packages.size

    fun addPackage(packageOne: Package) {
        packages.add(packageOne)
        notifyDataSetChanged()
    }

    class PackageViewHolder(private val binding: ItemPackageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(packageOne: Package) {
            binding.packageOne = packageOne
        }
    }

    fun setItems(newItems: List<Package>){
        packages.clear()
        packages.addAll(newItems)
        notifyDataSetChanged()
    }
}