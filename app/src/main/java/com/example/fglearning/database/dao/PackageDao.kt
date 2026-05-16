package com.example.fglearning.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.fglearning.database.entity.Package
import kotlinx.coroutines.flow.Flow

@Dao
interface PackageDao {
    @Query("SELECT * FROM packets")
    fun getAll(): Flow<List<Package>>

    @Query("SELECT * FROM packets WHERE id = :id")
    suspend fun getById(id: Long): Package?

    @Query("SELECT * FROM packets WHERE exercise = :exercise")
    fun getByExercise(exercise: Int): Flow<List<Package>>

    @Upsert()
    suspend fun insert(packet: Package)

    @Query("UPDATE packets SET recordCountCorrect = 0 WHERE id = :id")
    suspend fun resetRecord(id: Long)

    @Query("UPDATE packets SET recordCountCorrect = 0 WHERE id IN (:ids)")
    suspend fun resetRecords(ids: List<Long>)

    @Delete()
    suspend fun delete(packet: Package)
}