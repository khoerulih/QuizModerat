package com.khoerulih.quizmoderat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuizHistory(
    val id: String,
    val materiId: String,
    val materiTitle: String,
    val quizId: String,
    val score: Int,
    val timestamp: Long,
): Parcelable
