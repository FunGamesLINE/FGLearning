package com.example.fglearning.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.PackageItem

class ExerciseViewModel : ViewModel() {
    private val _exercise = MutableLiveData(0)
    private val _ids = MutableLiveData<List<Int>>()
    private val _element = MutableLiveData<PackageItem>()
    private val _flashcard = MutableLiveData<Flashcard>()
    private val _insertLetter = MutableLiveData<InsertLetter>()
    private val _accent = MutableLiveData<Accent>()

    val exercise: LiveData<Int> = _exercise
    val ids: LiveData<List<Int>> = _ids
    val element: LiveData<PackageItem> = _element
    val flashcard: LiveData<Flashcard> = _flashcard
    val insertLetter: LiveData<InsertLetter> = _insertLetter
    val accent: LiveData<Accent> = _accent
}