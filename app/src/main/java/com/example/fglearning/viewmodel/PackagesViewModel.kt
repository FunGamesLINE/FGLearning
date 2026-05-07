package com.example.fglearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fglearning.database.MaterialsDatabase
import com.example.fglearning.database.entity.Package
import com.example.fglearning.repository.PackageRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PackagesViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val packageRepository: PackageRepository by lazy {
        PackageRepository(MaterialsDatabase.getInstance(getApplication()).packageDao())
    }

    private val _packets = MutableLiveData<List<Package>>()
    val packets: LiveData<List<Package>> = _packets

    private var currentJob: Job? = null
    //private var currentExercise: Int = 0

    fun loadPackages(exercise: Int) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            packageRepository.getByExercise(exercise).collect { packageList ->
                _packets.value = packageList
            }
        }
    }
//    fun refresh() {
//        loadPackages(currentExercise)
//    }

    fun addPackage(packet: Package) {
        viewModelScope.launch {
            packageRepository.insert(packet)
        }
    }

    fun deletePackage(packet: Package) {
        viewModelScope.launch {
            packageRepository.delete(packet)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }
}