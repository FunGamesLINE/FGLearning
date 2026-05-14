package com.example.fglearning

import com.example.fglearning.database.entity.Accent
import com.example.fglearning.database.entity.Flashcard
import com.example.fglearning.database.entity.InsertLetter
import com.example.fglearning.database.entity.PackageItem

sealed class ExerciseData {
    data class Flashcard(val frontText: String, val backText: String) : ExerciseData()
    data class Accent(val word: String, val accentPos: Int) : ExerciseData()
    data class InsertLetter(val word: String, val gaps: String) : ExerciseData()
}

data class PackageItemWithData(
    val packageItem: PackageItem,
    val content: ExerciseData
)