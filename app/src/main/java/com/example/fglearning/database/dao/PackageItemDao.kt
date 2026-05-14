package com.example.fglearning.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.fglearning.database.entity.Package
import com.example.fglearning.database.entity.PackageItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PackageItemDao {
    @Query("SELECT * FROM elements")
    suspend fun getAll(): List<PackageItem>

    @Query("SELECT * FROM elements WHERE id = :id")
    suspend fun getById(id: Long): PackageItem?

    @Query("SELECT * FROM elements WHERE packetId = :packetId")
    fun getByPacketId(packetId: Long): Flow<List<PackageItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(packageItem: PackageItem): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(packageItems: List<PackageItem>)

    @Insert()
    suspend fun insert(packageItem: PackageItem): Long

    @Insert()
    suspend fun insert(packageItems: List<PackageItem>)

    @Upsert()
    suspend fun update(packageItem: PackageItem)

    @Upsert()
    suspend fun update(packageItems: List<PackageItem>)

    @Delete()
    suspend fun delete(packageItem: PackageItem)

    @Delete()
    suspend fun delete(packageItems: List<PackageItem>)
}