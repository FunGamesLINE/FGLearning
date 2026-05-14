package com.example.fglearning.repository

import com.example.fglearning.database.dao.PackageDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import kotlinx.coroutines.flow.Flow

class PackageItemRepository(private val packageItemDao: PackageItemDao) {
    suspend fun getAll(): List<PackageItem> = packageItemDao.getAll()

    suspend fun getById(id: Long): PackageItem? = packageItemDao.getById(id)

    fun getByPacketId(packetId: Long): Flow<List<PackageItem>> = packageItemDao.getByPacketId(packetId)

    suspend fun insertIgnore(packageItem: PackageItem): Long = packageItemDao.insertIgnore(packageItem)

    suspend fun insertIgnore(packageItems: List<PackageItem>) = packageItemDao.insertIgnore(packageItems)

    suspend fun insert(packageItem: PackageItem): Long {
        return if (packageItem.id == 0L) {
            //новый элемент
            packageItemDao.insert(packageItem)
        } else {
            //существующий элемент
            val existing = packageItemDao.getById(packageItem.id)
            if (existing == null) {
                packageItemDao.insert(packageItem)
            } else {
                packageItemDao.update(packageItem)
                packageItem.id
            }
        }
    }

    suspend fun insert(packageItems: List<PackageItem>) = packageItemDao.insert(packageItems)

    suspend fun delete(packageItem: PackageItem) = packageItemDao.delete(packageItem)

    suspend fun delete(packageItems: List<PackageItem>) = packageItemDao.delete(packageItems)
}