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
    val id: Int = 0,
    val packetId: Int = 0, //foreign
    val difficulty: Int = 0,
    val marked: Boolean = false,
    val notes: String,
    val lastViewTimestamp: Long
)