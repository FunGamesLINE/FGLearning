package com.example.fglearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fglearning.database.MaterialsDatabase
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import com.example.fglearning.repository.AccentRepository
import com.example.fglearning.repository.FlashcardRepository
import com.example.fglearning.repository.InsertLetterRepository
import com.example.fglearning.repository.PackageItemRepository
import com.example.fglearning.repository.PackageRepository
import kotlinx.coroutines.launch

class ExerciseViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val packageItemsRepository: PackageItemRepository by lazy {
        PackageItemRepository(MaterialsDatabase.getInstance(getApplication()).packageItemDao())
    }
    private val packageRepository: PackageRepository by lazy {
        PackageRepository(MaterialsDatabase.getInstance(getApplication()).packageDao())
    }
    private val flashcardRepository: FlashcardRepository by lazy {
        FlashcardRepository(MaterialsDatabase.getInstance(getApplication()).flashcardDao())
    }
    private val accentRepository: AccentRepository by lazy {
        AccentRepository(MaterialsDatabase.getInstance(getApplication()).accentDao())
    }
    private val insertLetterRepository: InsertLetterRepository by lazy {
        InsertLetterRepository(MaterialsDatabase.getInstance(getApplication()).insertLetterDao())
    }

    private val _element = MutableLiveData<PackageItem?>()
    val element: LiveData<PackageItem?> = _element

    private val _flashcard = MutableLiveData<Flashcard?>()
    val flashcard: LiveData<Flashcard?> = _flashcard
    private val _accent = MutableLiveData<Accent?>()
    val accent: LiveData<Accent?> = _accent

    private val _insertLetter = MutableLiveData<InsertLetter?>()
    val insertLetter: LiveData<InsertLetter?> = _insertLetter

    fun setCurrentPacketItem(packet: Package?, elemId: Int) {
        viewModelScope.launch {
            packet?.let {
                _element.value = packageItemsRepository.getById(elemId)
                _element.value?.let { element ->
                    when (packet.exercise) {
                        1 -> _flashcard.value = flashcardRepository.getById(elemId)
                        2 -> _accent.value = accentRepository.getById(elemId)
                        3 -> _insertLetter.value = insertLetterRepository.getById(elemId)
                    }
                }
            }
        }
    }

    fun setCurrentPacketItemNull() {
        _element.value = null
        _flashcard.value = null
        _accent.value = null
        _insertLetter.value = null
    }

    fun deletePackageItem(packetItem: PackageItem) {
        viewModelScope.launch {
            packageRepository.getById(packetItem.packetId)?.exercise.let { exercise ->
                when (exercise) {
                    1 -> flashcardRepository.deleteById(packetItem.id)
                    2 -> accentRepository.deleteById(packetItem.id)
                    3 -> insertLetterRepository.deleteById(packetItem.id)
                }
            }
            packageItemsRepository.delete(packetItem)
        }
    }

    fun addPackageItem(packetItem: PackageItem, flashcard: Flashcard) {
        viewModelScope.launch {
            packageItemsRepository.insert(packetItem)
            flashcardRepository.insert(flashcard)
        }
    }
    fun addPackageItem(packetItem: PackageItem, accent: Accent) {
        viewModelScope.launch {
            packageItemsRepository.insert(packetItem)
            accentRepository.insert(accent)
        }
    }
    fun addPackageItem(packetItem: PackageItem, insertLetter: InsertLetter) {
        viewModelScope.launch {
            packageItemsRepository.insert(packetItem)
            insertLetterRepository.insert(insertLetter)
        }
    }

    //TODO results
}