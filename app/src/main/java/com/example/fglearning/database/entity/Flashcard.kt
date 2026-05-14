package com.example.fglearning.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "flashcard",
    foreignKeys = [
        ForeignKey(
            entity = PackageItem::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Flashcard(
    @PrimaryKey
    val id: Long = 0, //foreign
    val frontText: String,
    val backText: String
)