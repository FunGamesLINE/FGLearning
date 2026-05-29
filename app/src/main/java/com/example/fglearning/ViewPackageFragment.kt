package com.example.fglearning

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
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

        var marked = sessionViewModel.currentPacket.value?.marked
        fun checkSaveButton() {
            val name = binding.packetName.text.toString()
            val currMarked = sessionViewModel.currentPacket.value?.marked
            val currName = sessionViewModel.currentPacket.value?.name
            if (name.isNotBlank() && (name != currName || marked != currMarked)) {
                binding.saveButton.visibility = View.VISIBLE
            }
            else {
                binding.saveButton.visibility = View.INVISIBLE
            }
        }

        sessionViewModel.exerciseType.observe(viewLifecycleOwner) { exerciseType ->
            binding.exerciseType.setExerciseType(exerciseType)
        }

        binding.packetName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkSaveButton()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.saveButton.setOnClickListener {
            val name = binding.packetName.text.toString()
            val exercise = sessionViewModel.exerciseType.value
            if (name.isNotEmpty() && exercise != null) {
                if (sessionViewModel.adding.value == true) {
                    lifecycleScope.launch {
                        val packet = Package(
                            name = name,
                            exercise = exercise
                        )
                        packagesViewModel.addPackage(packet)
                    }
                }
                else {
                    sessionViewModel.currentPacket.value?.let { packet ->
                        lifecycleScope.launch {
                            val updatedPacket = packet.copy(name = name, marked = marked ?: false)
                            packagesViewModel.addPackage(updatedPacket)
                            sessionViewModel.setCurrentPacket(updatedPacket)
                        }
                    }
                }
            }
            findNavController().popBackStack()
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.markButton.setOnClickListener {
            if (marked == true) {
                marked = false
                binding.markButton.setImageResource(R.drawable.bookmark)
            }
            else {
                marked = true
                binding.markButton.setImageResource(R.drawable.baseline_bookmark)
            }
            checkSaveButton()
        }

        binding.deleteButton.setOnClickListener {
            sessionViewModel.currentPacket.value?.let { packet ->
                val builder = AlertDialog.Builder(requireContext())
                    .setTitle("Подтверждение")
                    .setMessage("Вы действительно хотите удалить этот пакет?")
                    .setPositiveButton("Да") { dialog, _ ->
                        lifecycleScope.launch {
                            packagesViewModel.deletePackage(packet)
                        }
                        dialog.dismiss()
                        Toast.makeText(requireContext(), "Пакет \"${sessionViewModel.currentPacket.value?.name.toString()}\" удалён", Toast.LENGTH_SHORT).show()
                        sessionViewModel.setCurrentPacket(null)
                        findNavController().popBackStack(R.id.viewPackagesFragment, inclusive = false)
                    }
                    .setNegativeButton("Отмена") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alertDialog = builder.create()
                alertDialog.setOnShowListener {
                    val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
                    negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple))
                }
                alertDialog.show()
            }
        }

        sessionViewModel.currentPacket.observe(viewLifecycleOwner) { currentPacket ->
            currentPacket?.let {
                binding.packetName.setText(currentPacket.name)
                binding.correctAnswersRecord.text = currentPacket.recordCountCorrect.toString()
                binding.correctAnswersLastTime .text = currentPacket.lastCountCorrect.toString()
                binding.incorrectAnswersLastTime.text = currentPacket.lastCountIncorrect.toString()
                if (currentPacket.marked) binding.markButton.setImageResource(R.drawable.baseline_bookmark)
                else binding.markButton.setImageResource(R.drawable.bookmark)
            }
        }

        binding.difficultiesCount.visibility = View.GONE
        //TODO write code for difficultiesCount in ViewPackage fragment

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

    override fun onResume() {
        super.onResume()
        sessionViewModel.setViewListType(0)
    }
}