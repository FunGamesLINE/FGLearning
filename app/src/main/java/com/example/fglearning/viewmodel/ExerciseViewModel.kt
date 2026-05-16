package com.example.fglearning.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fglearning.ExerciseData
import com.example.fglearning.PackageItemWithData
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.Long

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

    //Current material
    private val _element = MutableLiveData<PackageItem?>()
    val element: LiveData<PackageItem?> = _element

    private val _flashcard = MutableLiveData<Flashcard?>()
    val flashcard: LiveData<Flashcard?> = _flashcard
    private val _accent = MutableLiveData<Accent?>()
    val accent: LiveData<Accent?> = _accent
    private val _insertLetter = MutableLiveData<InsertLetter?>()
    val insertLetter: LiveData<InsertLetter?> = _insertLetter


    //MutableList of all materials for this exercise
    private val packageItemsWithData = mutableListOf<PackageItemWithData>()
    private var currentIndex = 0

    private val _currentPackageItemWithData = MutableLiveData<PackageItemWithData?>()
    val currentPackageItemWithData: LiveData<PackageItemWithData?> = _currentPackageItemWithData

    //Statistics
    data class OldExerciseResults(
        val totalCorrect: Int = 0,
        val totalIncorrect: Int = 0,
        val recordCorrect: Int = 0,
        val countNotSelected: Int = 0,
        val countEasy: Int = 0,
        val countNotBad: Int = 0,
        val countBad: Int = 0,
        val countHard: Int = 0
    )
    private var oldExerciseResults: OldExerciseResults? = null
    private val scores = mutableMapOf<Long, Int>() //id, score
    private var totalCorrectCount: Int = 0
    private var totalIncorrectCount: Int = 0
    private val _doneCount = MutableLiveData<Int>(0)
    val doneCount: LiveData<Int> = _doneCount

    private var currentJob: Job? = null

    suspend fun countByPacketAndDifficulty(packetId: Long, difficulty: Int): Int {
        return packageItemsRepository.countByPacketAndDifficulty(packetId, difficulty)
    }

    fun countLeftItems(): Int {
        return packageItemsWithData.size
    }

    fun setCurrentPacketItem(packet: Package?, elemId: Long, onComplete: () -> Unit = {}) {
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
            onComplete()
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
            val elemId = packageItemsRepository.insert(packetItem)
            flashcardRepository.insert(
                flashcard.copy(id = elemId)
            )
        }
    }
    fun addPackageItem(packetItem: PackageItem, accent: Accent) {
        viewModelScope.launch {
            val elemId = packageItemsRepository.insert(packetItem)
            accentRepository.insert(
                accent.copy(id = elemId)
            )
        }
    }
    fun addPackageItem(packetItem: PackageItem, insertLetter: InsertLetter) {
        viewModelScope.launch {
            val elemId = packageItemsRepository.insert(packetItem)
            insertLetterRepository.insert(
                insertLetter.copy(id = elemId)
            )
        }
    }

    private suspend fun loadItemsWithRetry(packetId: Long, exerciseType: Int): List<PackageItemWithData> {
        //сложности 0,2,3,4 (учитывается только для флешкарточек)
        var itemsWithData = loadPacketItemsWithData(packetId, exerciseType, listOf(0, 2, 3, 4))

        //для флешкарточек, если ничего не нашли - ищем сложности 1,2
        if (itemsWithData.isEmpty() && exerciseType == 1) {
            itemsWithData = loadPacketItemsWithData(packetId, exerciseType, listOf(1))
        }

        return itemsWithData
    }

    private suspend fun loadPacketItemsWithData(packetId: Long, exerciseType: Int, difficulties: List<Int>): List<PackageItemWithData> {
        val packageItemsList = packageItemsRepository.getByPacketId(packetId).first()

        return packageItemsList.mapNotNull { packageItem ->
            val content = when (exerciseType) {
                1 -> {
                    val flashcard = flashcardRepository.getByIdAndDifficulty(packageItem.id, difficulties)
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
    }

    fun startExercise(packetId: Long, exerciseType: Int, oldResults: OldExerciseResults, onResult: (Boolean) -> Unit) {
        _doneCount.value = 0
        totalCorrectCount = 0
        totalIncorrectCount = 0
        scores.clear()
        oldExerciseResults = oldResults

        viewModelScope.launch {
            val items = loadItemsWithRetry(packetId, exerciseType)
            if (items.isNotEmpty()) {
                packageItemsWithData.clear()
                packageItemsWithData.addAll(items)

                val itemIds = packageItemsWithData.map { it.packageItem.id }
                scores.putAll(itemIds.associateWith { 0 })
                packageItemsRepository.resetLastCounts(itemIds)
                //packageItemsWithData.shuffle()
                //currentIndex = 0
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun setRandomPacketItem(): Boolean {
        if (packageItemsWithData.isEmpty()) {
            _currentPackageItemWithData.value = null
            Log.d("MYSetRandom", false.toString())
            return false
        }

        //random index, but set next index if random index == current index
        val randomIndex = (0 until packageItemsWithData.size).random()
        currentIndex = if (randomIndex == currentIndex && packageItemsWithData.size > 1) {
            (randomIndex + 1) % packageItemsWithData.size
            } else {
                randomIndex
            }

        _currentPackageItemWithData.value = packageItemsWithData[currentIndex]
        return true
    }

    suspend fun updateCurrentPackageItem(updatedPacketItem: PackageItem, exerciseType: Int, isCorrect: Boolean? = null) {
        _currentPackageItemWithData.value?.let { currentPackageItemWithData ->
            val packetItem = PackageItem(
                id = currentPackageItemWithData.packageItem.id,
                packetId = currentPackageItemWithData.packageItem.packetId,
                difficulty = updatedPacketItem.difficulty,
                marked = updatedPacketItem.marked,
                notes = updatedPacketItem.notes,
                lastViewTimestamp = System.currentTimeMillis() / (1000 * 60)
            )
            packageItemsRepository.insert(packetItem)
            val updatedItemWithData =
                currentPackageItemWithData.copy(packageItem = packetItem)
            packageItemsWithData[currentIndex] = updatedItemWithData

            val itemId = currentPackageItemWithData.packageItem.id
            when (exerciseType) {
                1 -> {
                    if (packageItemsWithData[currentIndex].packageItem.difficulty in listOf(1, 2)) {
                        packageItemsWithData.removeAll { it.packageItem.id == itemId }
                        scores.remove(itemId)
                    }
                }

                2, 3 -> {
                    if (isCorrect == true) {
                        scores[itemId] = (scores[itemId] ?: 0) + 1
                        scores[itemId]?.let { currentScore ->
                            if (currentScore >= 3) {
                                packageItemsWithData.removeAll { it.packageItem.id == itemId }
                                scores.remove(itemId)
                                _doneCount.value = (_doneCount.value ?: 0) + 1
                            }
                        }
                        totalCorrectCount++

                        packageItemsRepository.recordCorrectAnswer(itemId)
                    }
                    else {
                        scores[itemId]?.let {
                            if (it > 0) scores[itemId] = (scores[itemId] ?: 0) - 1
                            else scores[itemId] = 0
                        }
                        totalIncorrectCount++

                        packageItemsRepository.recordIncorrectAnswer(itemId)
                    }
                }
            }
        }
        Log.d("MYscoresMap", scores.toString())
    }

    fun getCurrentItemScores(): Int? {
        return scores[_currentPackageItemWithData.value?.packageItem?.id]
    }

    fun getOldExerciseResults(): OldExerciseResults? {
        return oldExerciseResults
    }

    fun getTotalCorrectCount(): Int {
        return totalCorrectCount
    }

    fun getTotalIncorrectCount(): Int {
        return totalIncorrectCount
    }
}