package com.example.fglearning.repository

import com.example.fglearning.database.dao.FlashcardDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter

class FlashcardRepository(private val flashcardDao: FlashcardDao) {
    suspend fun getById(id: Int): Flashcard? = flashcardDao.getById(id)
}