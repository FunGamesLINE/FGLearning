package com.example.fglearning.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packets")
data class Package(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val exercise: Int,
    val marked: Boolean = false,
    val lastCountCorrect: Int = 0,
    val lastCountIncorrect: Int = 0,
    val recordCountCorrect: Int = 0
)