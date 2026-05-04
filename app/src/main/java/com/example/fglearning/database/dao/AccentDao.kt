package com.example.fglearning.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.PackageItem

@Dao
interface AccentDao {
    @Query("SELECT * FROM accent")
    suspend fun getAll(): List<Accent>

    @Query("SELECT * FROM accent WHERE id = :id")
    suspend fun getById(id: Int): Accent?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(accent: Accent)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(accents: List<Accent>)

    @Upsert()
    suspend fun insert(accent: Accent)

    @Upsert()
    suspend fun insert(accents: List<Accent>)

    @Delete()
    suspend fun delete(accent: Accent)

    @Delete()
    suspend fun delete(accents: List<Accent>)
}