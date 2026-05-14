package com.example.fglearning.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcard")
    suspend fun getAll(): List<Flashcard>

    @Query("SELECT * FROM flashcard WHERE id = :id")
    suspend fun getById(id: Long): Flashcard?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(flashcard: Flashcard): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(flashcards: List<Flashcard>)

    @Upsert()
    suspend fun insert(flashcard: Flashcard)

    @Upsert()
    suspend fun insert(flashcards: List<Flashcard>)

    @Delete()
    suspend fun delete(flashcard: Flashcard)

    @Delete()
    suspend fun delete(flashcards: List<Flashcard>)

    @Query("DELETE FROM flashcard WHERE id = :id")
    suspend fun deleteById(id: Long)
}