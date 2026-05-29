package com.example.fglearning.fragments.viewList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
//import com.example.fglearning.setExerciseType
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
            sessionViewModel.exerciseType.value,
            onItemClick = { item ->
                sessionViewModel.setAdding(false)
                exerciseViewModel.setCurrentPacketItem(
                    sessionViewModel.currentPacket.value,
                    item.packageItem.id
                ) {
                    findNavController().navigate(R.id.action_viewPackageItemsAllFragment_to_viewPackageItemFragment)
                }
            },
            onMarkClick = { item ->
                lifecycleScope.launch {
                    val updatedPacketItem = item.packageItem.copy(marked = !item.packageItem.marked)
                    packageItemsViewModel.addPacketItem(updatedPacketItem)
                }
            }
        )
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        packageItemsViewModel.packageItemsWithData.observe(viewLifecycleOwner) { packageItemsWithData ->
            adapter.setItems(packageItemsWithData)
        }

        sessionViewModel.currentPacket.observe(viewLifecycleOwner) { currentPacket ->
            currentPacket?.let {
                binding.packetNameText.text = currentPacket.name
                packageItemsViewModel.loadPacketItems(currentPacket.id, currentPacket.exercise)
            }
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

    override fun onResume() {
        super.onResume()
        sessionViewModel.setViewListType(1)
    }
}