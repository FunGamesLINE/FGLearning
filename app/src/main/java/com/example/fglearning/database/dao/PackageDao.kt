package com.example.fglearning.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.fglearning.database.entity.Package

@Dao
interface PackageDao {
    @Query("SELECT * FROM packets")
    suspend fun getAll(): List<Package>

    @Query("SELECT * FROM packets WHERE id = :id")
    suspend fun getById(id: Int): Package?

    @Upsert()
    suspend fun insert(packet: Package)

    @Delete()
    suspend fun delete(packet: Package)
}