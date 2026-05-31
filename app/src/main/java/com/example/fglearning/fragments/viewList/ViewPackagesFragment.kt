package com.example.fglearning.fragments.viewList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fglearning.R
import com.example.fglearning.adapter.PackageAdapter
import com.example.fglearning.database.entity.Package
import com.example.fglearning.databinding.FragmentViewPackagesBinding
import com.example.fglearning.setExerciseType
import com.example.fglearning.viewmodel.ExerciseViewModel
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
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackagesBinding.inflate(layoutInflater)

        fun startExercise(packet: Package) {
            lifecycleScope.launch {
                val countNotSelected = exerciseViewModel.countByPacketAndDifficulty(packet.id, 0)
                val countEasy = exerciseViewModel.countByPacketAndDifficulty(packet.id, 1)
                val countNotBad = exerciseViewModel.countByPacketAndDifficulty(packet.id, 2)
                val countBad = exerciseViewModel.countByPacketAndDifficulty(packet.id, 3)
                val countHard = exerciseViewModel.countByPacketAndDifficulty(packet.id, 4)

                val oldExerciseResults = ExerciseViewModel.OldExerciseResults(
                    totalCorrect = packet.lastCountCorrect,
                    totalIncorrect = packet.lastCountIncorrect,
                    recordCorrect = packet.recordCountCorrect,
                    countNotSelected = countNotSelected,
                    countEasy = countEasy,
                    countNotBad = countNotBad,
                    countBad = countBad,
                    countHard = countHard
                )

                exerciseViewModel.startExercise(packet.id, packet.exercise, oldExerciseResults) { success ->
                    Log.d("MYStartingExercise", success.toString())
                    if (success) {
                        exerciseViewModel.setRandomPacketItem()
                        sessionViewModel.startedExercise()
                        when (sessionViewModel.exerciseType.value) {
                            1 -> findNavController().navigate(R.id.action_viewPackagesFragment_to_runFlashcardsFragment)
                            2 -> findNavController().navigate(R.id.action_viewPackagesFragment_to_runAccentFragment)
                            3 -> findNavController().navigate(R.id.action_viewPackagesFragment_to_runInsertlettersFragment)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Нет материалов для упражнения! Чтобы добавить их, нажмите на пакет", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        val adapter = PackageAdapter(
            mutableListOf(),
            onItemClick = { packet ->
                if (sessionViewModel.shouldShowResults.value != true && sessionViewModel.shouldStartExercise.value != true) {
                    sessionViewModel.setCurrentPacket(packet)
                    findNavController().navigate(R.id.action_viewPackagesFragment_to_viewPackageItemsAllFragment)
                }
            },
            onMarkClick = { packet ->
                lifecycleScope.launch {
                    val updatedPacket = packet.copy(marked = !packet.marked)
                    packagesViewModel.addPackage(updatedPacket)
                }
            },
            onPlayClick = { packet ->
                if (sessionViewModel.shouldShowResults.value != true) {
                    sessionViewModel.setCurrentPacket(packet)
                    sessionViewModel.startExercise()
                }
                //startExercise(packet)
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
            if (exercise != null) {
                packagesViewModel.loadPackages(exercise)
            }
        }

        sessionViewModel.shouldShowResults.observe(viewLifecycleOwner) { shouldShowResults ->
            if (shouldShowResults)
                findNavController().navigate(R.id.action_viewPackagesFragment_to_resultsFragment)
        }

        sessionViewModel.shouldStartExercise.observe(viewLifecycleOwner) { shouldStartExercise ->
            if (shouldStartExercise) {
                sessionViewModel.currentPacket.value?.let { currentPacket ->
                    startExercise(currentPacket)
                }
            }
        }

        binding.addPackageButton.setOnClickListener {
            sessionViewModel.setAdding(true)
            findNavController().navigate(R.id.action_viewPackagesFragment_to_viewPackageFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        sessionViewModel.setViewListType(0)
        sessionViewModel.setIsExerciseWorking(false)
    }
}