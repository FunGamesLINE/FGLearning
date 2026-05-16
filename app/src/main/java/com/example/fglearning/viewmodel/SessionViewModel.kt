package com.example.fglearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fglearning.database.MaterialsDatabase
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import com.example.fglearning.repository.PackageRepository

class SessionViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val packageRepository: PackageRepository by lazy {
        PackageRepository(MaterialsDatabase.getInstance(getApplication()).packageDao())
    }

    private val _currentPacket = MutableLiveData<Package?>()
    val currentPacket: LiveData<Package?> = _currentPacket

    //private val _currentPacketItem = MutableLiveData<PackageItem?>()
    //val currentPacketItem: LiveData<PackageItem?> = _currentPacketItem

    private val _exerciseType = MutableLiveData(1)
    val exerciseType: LiveData<Int> = _exerciseType

    private val _shouldShowResults = MutableLiveData(false)
    val shouldShowResults: LiveData<Boolean> = _shouldShowResults

    private val _shouldStartExercise = MutableLiveData(false)
    val shouldStartExercise: LiveData<Boolean> = _shouldStartExercise
    private val _shouldFinishExercise = MutableLiveData(false)
    val shouldFinishExercise: LiveData<Boolean> = _shouldFinishExercise

    private val _adding = MutableLiveData(false)
    val adding: LiveData<Boolean> = _adding

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun finishExercise() {
        _shouldShowResults.value = true
        _shouldFinishExercise.value = true
    }

    fun startExercise() {
        _shouldFinishExercise.value = false
        _shouldStartExercise.value = true
    }

    fun shownResults() {
        _shouldShowResults.value = false
        _shouldFinishExercise.value = false
    }

    fun startedExercise() {
        _shouldStartExercise.value = false
    }

    fun setAdding(adding: Boolean) {
        _adding.value = adding
    }

    fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun setExerciseType(exercise: Int) {
        _exerciseType.value = exercise
    }

    fun setCurrentPacket(packet: Package?) {
        _currentPacket.value = packet
    }

    suspend fun resetCurrentPackageRecord() {
        _currentPacket.value?.let { currentPacket ->
            packageRepository.setRecord(currentPacket.id, 0)
        }
    }

    suspend fun setCurrentPackageRecord(newRecord: Int) {
        _currentPacket.value?.let { currentPacket ->
            packageRepository.setRecord(currentPacket.id, newRecord)
        }
    }

//    fun setCurrentPacketItem(packetItem: PackageItem) {
//        _currentPacketItem.value = packetItem
//    }

    //fun getAdding() = adding.value ?: false
}