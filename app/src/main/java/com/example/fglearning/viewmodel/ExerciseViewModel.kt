package com.example.fglearning.viewmodel

import android.app.Application
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
    private val scores = mutableMapOf<Long, Int>()

    //Statistics
    data class OldExerciseResults(
        val totalCorrect: Int = 0,
        val totalIncorrect: Int = 0,
        val recordCorrect: Int = 0,
        val countNotSelected: Int = 0,
        val countEasy: Int = 0,
        val countNotBad: Int = 0,
        val countBad: Int = 0,
        val countVeryHard: Int = 0
    )
    private val oldExerciseResults: OldExerciseResults? = null
    private val scoresMap = mutableMapOf<Long, Int>() //id, score
    private val totalCorrectCount: Int = 0
    private val totalIncorrectCount: Int = 0
    private val _doneCount = MutableLiveData<Int>()
    val doneCount: LiveData<Int> = _doneCount

    /*
    TODO
    - очки за каждый элемент - если ответ верный, к элементу прибавляется очко, если нет, отнимается
    - общее количество верных и неверных ответов за сессию
    - множество id-шников элементов которые вышли из участия упражнения в процессе его выполнения

    - старые результаты (количество элементов различных сложностей (5 полей), пр/непр ответов всего из прошлой сессии, рекордное количество в начале упражнения)


    Менять (только для тех, кто участвует):
    - количество правильных и неправильных ответов за эту сессию для каждого элемента
    - количество пр/непр всего
    - для пакета количество пр/непр за сессию тоже менять
    - рекордное количество верных (если реально рекорд)


    - Инициализация упражнения - всё по нулям, все нужные переменные инициализированны, все слова/карточки получены
    - Установка нового материала - рандом из списка (если остался 1 - то тот же, если несколько - обязательно другой)
    - Обновление текущего материала в зависимости от ответа -
        всегда обновлять последнее время просмотра
        верно (увеличить очки, если очков достаточно исключить из списка и обновить количество выполненных, обновить все переменные статистики (типа общего количества верных)) неверно (уменьшить очки, но не меньше 0, , обновить все переменные статистики (типа общего количества неверных))
        для флешкарт учитывается только изменение сложности - легко, неплохо (исключить из списка) / плохо, сложно, не выбрано, снова (просто оставить в списке)
            если все карточки были "неплохо" или "легко", но пользователь запустил приложение, значит нужно запустить флешкарты с теми же правилами
        //если все карточки были "неплохо", но пользователь запустил приложение, значит нужно "неплохо" оставлять и только легко отсеивать
        //сортировка по последнему просмотру
     */

    private var currentJob: Job? = null

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


    fun loadPacketItemsWithData(packetId: Long, exerciseType: Int) {
        currentJob?.cancel()

        currentJob = viewModelScope.launch {
            packageItemsRepository.getByPacketId(packetId).collect { packageItemsList ->
                packageItemsWithData.clear()
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
                packageItemsWithData.addAll(itemsWithData)
            }
        }
    }
}