package com.example.fglearning.repository

import com.example.fglearning.database.dao.PackageDao
import com.example.fglearning.database.dao.PackageItemDao
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import kotlinx.coroutines.flow.Flow

class PackageItemRepository(private val packageItemDao: PackageItemDao) {

//    suspend fun getAll(): List<PackageItem>
//
//    suspend fun getById(id: Int): PackageItem?
//
//    suspend fun insertIgnore(packageItem: PackageItem)
//
//    suspend fun insertIgnore(packageItems: List<PackageItem>)
//
//    suspend fun insert(packageItem: PackageItem)
//
//    suspend fun insert(packageItems: List<PackageItem>)
//
//    suspend fun delete(packageItem: PackageItem)
//
//    suspend fun delete(packageItems: List<PackageItem>)
}