package com.example.fglearning.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "insertLetter",
    foreignKeys = [
        ForeignKey(
            entity = PackageItem::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InsertLetter(
    @PrimaryKey
    val id: Long = 0, //foreign
    val word: String,
    val gaps: String
)