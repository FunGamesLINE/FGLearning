package com.example.fglearning.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem

class SessionViewModel : ViewModel() {
    private val _currentPacket = MutableLiveData<Package?>()
    val currentPacket: LiveData<Package?> = _currentPacket

    //private val _currentPacketItem = MutableLiveData<PackageItem?>()
    //val currentPacketItem: LiveData<PackageItem?> = _currentPacketItem

    private val _exerciseType = MutableLiveData(1)
    val exerciseType: LiveData<Int> = _exerciseType

    private val _shouldShowResults = MutableLiveData(false)
    val shouldShowResults: LiveData<Boolean> = _shouldShowResults

    private val _adding = MutableLiveData(false)
    val adding: LiveData<Boolean> = _adding

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    fun finishExercise() {
        _shouldShowResults.value = true
    }

    fun afterResults() {
        _shouldShowResults.value = false
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

//    fun setCurrentPacketItem(packetItem: PackageItem) {
//        _currentPacketItem.value = packetItem
//    }

    //fun getAdding() = adding.value ?: false
}