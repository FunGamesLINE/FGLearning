package com.example.fglearning

import android.widget.TextView

fun TextView.setExerciseType(exerciseType: Int) {
    text = when (exerciseType) {
        1 -> "Флеш-карточки"
        2 -> "Постановка ударения"
        3 -> "Вставка букв"
        else -> "???"
    }
}