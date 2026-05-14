package com.example.fglearning.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.InsertLetter

@Dao
interface InsertLetterDao {
    @Query("SELECT * FROM insertLetter")
    suspend fun getAll(): List<InsertLetter>

    @Query("SELECT * FROM insertLetter WHERE id = :id")
    suspend fun getById(id: Long): InsertLetter?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(insertLetter: InsertLetter): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(insertLetterList: List<InsertLetter>)

    @Upsert()
    suspend fun insert(insertLetter: InsertLetter)

    @Upsert()
    suspend fun insert(insertLetterList: List<InsertLetter>)

    @Delete()
    suspend fun delete(insertLetter: InsertLetter)

    @Delete()
    suspend fun delete(insertLetterList: List<InsertLetter>)

    @Query("DELETE FROM insertLetter WHERE id = :id")
    suspend fun deleteById(id: Long)
}