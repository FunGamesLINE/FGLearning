package com.example.fglearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fglearning.adapter.PackageAdapter
import com.example.fglearning.database.entity.Package
import com.example.fglearning.databinding.FragmentViewPackageBinding
import com.example.fglearning.viewmodel.PackagesViewModel
import com.example.fglearning.viewmodel.SessionViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewPackageFragment : Fragment() {
    lateinit var binding: FragmentViewPackageBinding
    private val sessionViewModel: SessionViewModel by activityViewModels()
    private val packagesViewModel: PackagesViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackageBinding.inflate(layoutInflater)

        sessionViewModel.exerciseType.observe(viewLifecycleOwner) { exerciseType ->
            binding.exerciseType.setExerciseType(exerciseType)
        }

        binding.saveButton.setOnClickListener {
            val name = binding.packetName.text.toString()
            val exercise = sessionViewModel.exerciseType.value
            if (name.isNotEmpty() && exercise != null) {
                lifecycleScope.launch {
                    val packet = Package(
                        name = name,
                        exercise = exercise
                    )
                    packagesViewModel.addPackage(packet)
                }
            }
            findNavController().popBackStack()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.markButton.setOnClickListener {
            sessionViewModel.currentPacket.value?.let { packet ->
                lifecycleScope.launch {
                    val updatedPacket = packet.copy(marked = !packet.marked)
                    packagesViewModel.addPackage(updatedPacket)
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            sessionViewModel.currentPacket.value?.let { packet ->
                lifecycleScope.launch {
                    packagesViewModel.deletePackage(packet)
                }
            }
        }

        sessionViewModel.currentPacket.observe(viewLifecycleOwner) { currentPacket ->
            currentPacket?.let {
                binding.packetName.setText(currentPacket.name)
                binding.correctAnswersRecord.text = currentPacket.recordCountCorrect.toString()
                binding.correctAnswersLastTime .text = currentPacket.lastCountCorrect.toString()
                binding.incorrectAnswersLastTime.text = currentPacket.lastCountIncorrect.toString()
            }
        }

        sessionViewModel.adding.observe(viewLifecycleOwner) { isAdding ->
            if (isAdding) {
                sessionViewModel.setCurrentPacket(null)
                binding.deleteButton.visibility = View.GONE
                binding.markButton.visibility = View.GONE
                binding.stats.visibility = View.GONE
                binding.packetName.setText("")
            } else {
                binding.deleteButton.visibility = View.VISIBLE
                binding.markButton.visibility = View.VISIBLE
                binding.stats.visibility = View.VISIBLE
                sessionViewModel.currentPacket.value?.let { currentPacket ->
                    binding.packetName.setText(currentPacket.name)
                }
            }
        }

        return binding.root
    }
}