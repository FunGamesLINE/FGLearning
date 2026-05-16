package com.example.fglearning.repository

import androidx.room.Query
import com.example.fglearning.database.dao.PackageDao
import com.example.fglearning.database.entity.Package
import kotlinx.coroutines.flow.Flow

class PackageRepository(private val packageDao: PackageDao) {
    fun getByExercise(exercise: Int): Flow<List<Package>> = packageDao.getByExercise(exercise)

    fun getAll(): Flow<List<Package>> = packageDao.getAll()

    suspend fun getById(id: Long): Package? = packageDao.getById(id)

    suspend fun insert(packet: Package) = packageDao.insert(packet)

    suspend fun setRecord(id: Long, newRecord: Int) = packageDao.setRecord(id, newRecord)

    suspend fun resetRecords(ids: List<Long>) = packageDao.resetRecords(ids)

    suspend fun delete(packet: Package) = packageDao.delete(packet)
}