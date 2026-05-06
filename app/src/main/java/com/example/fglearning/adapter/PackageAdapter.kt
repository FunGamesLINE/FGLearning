package com.example.fglearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fglearning.R
import com.example.fglearning.databinding.ItemPackageBinding
import com.example.fglearning.database.entity.Package

class PackageAdapter(
    val packages: MutableList<Package>,
    val onItemClick: ((Package) -> Unit)? = null,
    val onPlayClick: ((Package) -> Unit)? = null,
    val onMarkClick: ((Package) -> Unit)? = null,
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

    inner class PackageViewHolder(private val binding: ItemPackageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(packageOne: Package) {
            binding.packageOne = packageOne
            binding.packetNameText.text = packageOne.name
            if (packageOne.marked) binding.markButton.setImageResource(R.drawable.baseline_bookmark)
            else binding.markButton.setImageResource(R.drawable.bookmark)

            binding.root.setOnClickListener {
                onItemClick?.invoke(packageOne)
            }

            binding.playButton.setOnClickListener {
                onPlayClick?.invoke(packageOne)
            }

            binding.markButton.setOnClickListener {
                onMarkClick?.invoke(packageOne)
            }
            //TODO
        }
    }

    fun setItems(newItems: List<Package>){
        packages.clear()
        packages.addAll(newItems)
        notifyDataSetChanged()
    }
}