package com.example.fglearning.repository

import com.example.fglearning.database.dao.PackageDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import kotlinx.coroutines.flow.Flow

class PackageItemRepository(private val packageItemDao: PackageItemDao) {
    suspend fun getAll(): List<PackageItem> = packageItemDao.getAll()

    suspend fun getById(id: Int): PackageItem? = packageItemDao.getById(id)

    fun getByPacketId(packetId: Int): Flow<List<PackageItem>> = packageItemDao.getByPacketId(packetId)

    suspend fun insertIgnore(packageItem: PackageItem) = packageItemDao.insertIgnore(packageItem)

    suspend fun insertIgnore(packageItems: List<PackageItem>) = packageItemDao.insertIgnore(packageItems)

    suspend fun insert(packageItem: PackageItem) = packageItemDao.insert(packageItem)

    suspend fun insert(packageItems: List<PackageItem>) = packageItemDao.insert(packageItems)

    suspend fun delete(packageItem: PackageItem) = packageItemDao.delete(packageItem)

    suspend fun delete(packageItems: List<PackageItem>) = packageItemDao.delete(packageItems)
}