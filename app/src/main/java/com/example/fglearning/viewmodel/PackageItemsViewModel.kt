package com.example.fglearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fglearning.ExerciseData
import com.example.fglearning.PackageItemWithData
import com.example.fglearning.database.MaterialsDatabase
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import com.example.fglearning.repository.AccentRepository
import com.example.fglearning.repository.FlashcardRepository
import com.example.fglearning.repository.InsertLetterRepository
import com.example.fglearning.repository.PackageItemRepository
import com.example.fglearning.repository.PackageRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PackageItemsViewModel(
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

    private val _packageItemsWithData = MutableLiveData<List<PackageItemWithData>>()
    val packageItemsWithData: LiveData<List<PackageItemWithData>> = _packageItemsWithData

    private var currentJob: Job? = null

    fun loadPacketItems(packetId: Long, exerciseType: Int) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            packageItemsRepository.getByPacketId(packetId).collect { packageItemsList ->
                val itemsWithData = packageItemsList.mapNotNull { packageItem ->
                    val content = when (exerciseType) {
                        1 -> {
                            val flashcard = flashcardRepository.getById(packageItem.id)
                            flashcard?.let {
                                ExerciseData.Flashcard(
                                    frontText = it.frontText,
                                    backText = it.backText
                                )
                            }
                        }
                        2 -> {
                            val accent = accentRepository.getById(packageItem.id)
                            accent?.let {
                                ExerciseData.Accent(
                                    word = it.word,
                                    accentPos = it.accentPos
                                )
                            }
                        }
                        3 -> {
                            val insertLetter = insertLetterRepository.getById(packageItem.id)
                            insertLetter?.let {
                                ExerciseData.InsertLetter(
                                    word = it.word,
                                    gaps = it.gaps
                                )
                            }
                        }
                        else -> null
                    }

                    content?.let {
                        PackageItemWithData(
                            packageItem = packageItem,
                            content = it
                        )
                    }
                }
                _packageItemsWithData.value = itemsWithData
            }
        }
    }

    fun addPacketItem(packetItem: PackageItem) {
        viewModelScope.launch {
            packageItemsRepository.insert(packetItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        currentJob?.cancel()
    }

}