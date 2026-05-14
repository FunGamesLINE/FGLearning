package com.example.fglearning.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "elements",
    foreignKeys = [
        ForeignKey(
            entity = Package::class,
            parentColumns = ["id"],
            childColumns = ["packetId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PackageItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val packetId: Long, //foreign
    val difficulty: Int = 0,
    val marked: Boolean = false,
    val notes: String = "",
    val lastViewTimestamp: Long = -1,
    val lastCountCorrect: Int = 0,
    val lastCountIncorrect: Int = 0,
    val totalCountCorrect: Int = 0,
    val totalCountIncorrect: Int = 0
)