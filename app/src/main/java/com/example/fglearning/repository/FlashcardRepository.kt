package com.example.fglearning.repository

import com.example.fglearning.database.dao.FlashcardDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter

class FlashcardRepository(private val flashcardDao: FlashcardDao) {
    suspend fun getById(id: Long): Flashcard? = flashcardDao.getById(id)

    suspend fun getByIdAndDifficulty(id: Long, difficulties: List<Int>): Flashcard? = flashcardDao.getByIdAndDifficulty(id, difficulties)

    suspend fun deleteById(id: Long) = flashcardDao.deleteById(id)

    suspend fun insert(flashcard: Flashcard) = flashcardDao.insert(flashcard)

    suspend fun insert(flashcards: List<Flashcard>) = flashcardDao.insert(flashcards)
}