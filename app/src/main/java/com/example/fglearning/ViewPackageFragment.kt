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
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        sessionViewModel.adding.observe(viewLifecycleOwner) { isAdding ->
            if (isAdding) {
                binding.deleteButton.visibility = View.GONE
                binding.markButton.visibility = View.GONE
            } else {
                binding.deleteButton.visibility = View.VISIBLE
                binding.markButton.visibility = View.VISIBLE
            }
        }

        return binding.root
    }
}