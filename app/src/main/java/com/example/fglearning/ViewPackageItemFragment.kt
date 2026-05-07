package com.example.fglearning

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.fglearning.databinding.FragmentViewPackageItemBinding

class ViewPackageItemFragment : Fragment() {
    lateinit var binding: FragmentViewPackageItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentViewPackageItemBinding.inflate(layoutInflater)

        val items = listOf("Легко", "Неплохо", "Плохо", "Сложно")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            items
        )
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = items[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        //TODO val position = binding.spinner.selectedItemPosition

        return binding.root
    }
}