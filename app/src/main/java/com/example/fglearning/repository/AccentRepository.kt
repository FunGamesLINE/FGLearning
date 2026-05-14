package com.example.fglearning.repository

import com.example.fglearning.database.dao.AccentDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter

class AccentRepository(private val accentDao: AccentDao) {
    suspend fun getById(id: Long): Accent? = accentDao.getById(id)

    suspend fun deleteById(id: Long) = accentDao.deleteById(id)

    suspend fun insert(accent: Accent) = accentDao.insert(accent)

    suspend fun insert(accents: List<Accent>) = accentDao.insert(accents)
}