package com.example.fglearning.repository

import com.example.fglearning.database.dao.InsertLetterDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.PackageItem

class InsertLetterRepository(private val insertLetterDao: InsertLetterDao) {
    suspend fun getById(id: Long): InsertLetter? = insertLetterDao.getById(id)

    suspend fun deleteById(id: Long) = insertLetterDao.deleteById(id)

    suspend fun insert(insertLetter: InsertLetter) = insertLetterDao.insert(insertLetter)

    suspend fun insert(insertLetterList: List<InsertLetter>) = insertLetterDao.insert(insertLetterList)
}