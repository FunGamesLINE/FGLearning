package com.example.fglearning.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "accent",
    foreignKeys = [
        ForeignKey(
            entity = PackageItem::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Accent(
    @PrimaryKey
    val id: Long = 0, //foreign
    val word: String,
    val accentPos: Int
)