package com.example.fglearning.fragments.viewList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fglearning.R
import com.example.fglearning.adapter.PackageAdapter
import com.example.fglearning.databinding.FragmentViewPackagesBinding
import com.example.fglearning.setExerciseType
import com.example.fglearning.viewmodel.PackagesViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.getValue

class ViewPackagesFragment : Fragment() {
    lateinit var binding: FragmentViewPackagesBinding
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val packagesViewModel: PackagesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackagesBinding.inflate(layoutInflater)

        val adapter = PackageAdapter(
            mutableListOf(),
            onItemClick = { packet ->
                sessionViewModel.setCurrentPacket(packet)
                findNavController().navigate(R.id.action_viewPackagesFragment_to_viewPackageItemsAllFragment)
            },
            onMarkClick = { packet ->
                lifecycleScope.launch {
                    val updatedPacket = packet.copy(marked = !packet.marked)
                    packagesViewModel.addPackage(updatedPacket)
                }
            },
            onPlayClick = { packet ->
                when (sessionViewModel.exerciseType.value) {
                    1 -> findNavController().navigate(R.id.action_viewPackagesFragment_to_runFlashcardsFragment)
                    2 -> findNavController().navigate(R.id.action_viewPackagesFragment_to_runAccentFragment)
                    3 -> findNavController().navigate(R.id.action_viewPackagesFragment_to_runInsertlettersFragment)
                }
            }
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        packagesViewModel.packets.observe(viewLifecycleOwner) { packets ->
            adapter.setItems(packets)
        }

        sessionViewModel.exerciseType.observe(viewLifecycleOwner) { exercise ->
            binding.exerciseTypeName.setExerciseType(exercise)
            if (exercise != null) {
                packagesViewModel.loadPackages(exercise)
            }
        }

        binding.addPackageButton.setOnClickListener {
            sessionViewModel.setAdding(true)
            findNavController().navigate(R.id.action_viewPackagesFragment_to_viewPackageFragment)
        }

        return binding.root
    }
}