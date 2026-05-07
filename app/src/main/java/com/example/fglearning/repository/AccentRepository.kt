package com.example.fglearning.repository

import com.example.fglearning.database.dao.AccentDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard

class AccentRepository(private val accentDao: AccentDao) {
    suspend fun getById(id: Int): Accent? = accentDao.getById(id)
}