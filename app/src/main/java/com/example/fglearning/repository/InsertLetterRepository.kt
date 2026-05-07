package com.example.fglearning.repository

import com.example.fglearning.database.dao.InsertLetterDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.PackageItem

class InsertLetterRepository(private val insertLetterDao: InsertLetterDao) {
    suspend fun getById(id: Int): InsertLetter? = insertLetterDao.getById(id)
}