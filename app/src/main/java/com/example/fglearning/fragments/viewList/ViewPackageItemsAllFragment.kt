package com.example.fglearning.fragments.viewList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fglearning.R
import com.example.fglearning.adapter.PackageAdapter
import com.example.fglearning.adapter.PackageItemAdapter
import com.example.fglearning.database.entity.PackageItem
import com.example.fglearning.databinding.FragmentViewPackageItemsAllBinding
import com.example.fglearning.setExerciseType
import com.example.fglearning.viewmodel.ExerciseViewModel
import com.example.fglearning.viewmodel.PackageItemsViewModel
import com.example.fglearning.viewmodel.PackagesViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class ViewPackageItemsAllFragment : Fragment() {
    lateinit var binding: FragmentViewPackageItemsAllBinding
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val packageItemsViewModel: PackageItemsViewModel by activityViewModels()
    private val packagesViewModel: PackagesViewModel by activityViewModels()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackageItemsAllBinding.inflate(layoutInflater)

        val adapter = PackageItemAdapter(
            mutableListOf(),
            onItemClick = { packetItem ->
                exerciseViewModel.setCurrentPacketItem(sessionViewModel.currentPacket.value, packetItem.id)
                findNavController().navigate(R.id.action_viewPackageItemsAllFragment_to_viewPackageItemFragment)
            },
            onMarkClick = { packetItem ->
                lifecycleScope.launch {
                    val updatedPacketItem = packetItem.copy(marked = !packetItem.marked)
                    packageItemsViewModel.addPacketItem(updatedPacketItem)
                }
            }
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        packageItemsViewModel.packageItems.observe(viewLifecycleOwner) { packageItems ->
            adapter.setItems(packageItems)
        }

        sessionViewModel.currentPacket.observe(viewLifecycleOwner) { currentPacket ->
            currentPacket?.let {
                binding.packetNameText.text = currentPacket.name
                packageItemsViewModel.loadPacketItems(currentPacket.id)
            }
        }

        sessionViewModel.exerciseType.observe(viewLifecycleOwner) { exercise ->
            binding.exerciseTypeName.setExerciseType(exercise)
        }

        binding.addMaterialButton.setOnClickListener {
            sessionViewModel.setAdding(true)
            findNavController().navigate(R.id.action_viewPackageItemsAllFragment_to_viewPackageItemFragment)
        }

        binding.editPacketButton.setOnClickListener {
            sessionViewModel.setAdding(false)
            findNavController().navigate(R.id.action_viewPackageItemsAllFragment_to_viewPackageFragment)
        }

        return binding.root
    }
}