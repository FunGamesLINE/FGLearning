package com.example.fglearning.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fglearning.database.entity.Package

class SessionViewModel : ViewModel() {
    private val _currentPacket = MutableLiveData<Package?>()
    val currentPacket: LiveData<Package?> = _currentPacket

    private val _exerciseType = MutableLiveData(0)
    val exerciseType: LiveData<Int> = _exerciseType

    private val _shouldShowResults = MutableLiveData(false)
    val shouldShowResults: LiveData<Boolean> = _shouldShowResults

    private val _adding = MutableLiveData(false)
    val adding: LiveData<Boolean> = _adding



    fun finishExercise() {
        _shouldShowResults.value = true
    }

    fun afterResults() {
        _shouldShowResults.value = false
    }

    fun setAdding(adding: Boolean) {
        _adding.value = adding
    }

    fun setExerciseType(exercise: Int) {
        _exerciseType.value = exercise
    }

    fun getAdding() = adding.value ?: false
}