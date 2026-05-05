package com.example.fglearning.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.PackageItem

class ExerciseViewModel : ViewModel() {
    private val _element = MutableLiveData<PackageItem>()
    val element: LiveData<PackageItem> = _element

    private val _flashcard = MutableLiveData<Flashcard>()
    val flashcard: LiveData<Flashcard> = _flashcard
    private val _insertLetter = MutableLiveData<InsertLetter>()
    val insertLetter: LiveData<InsertLetter> = _insertLetter
    private val _accent = MutableLiveData<Accent>()
    val accent: LiveData<Accent> = _accent

    //TODO results





}