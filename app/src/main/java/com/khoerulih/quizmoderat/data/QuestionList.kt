package com.khoerulih.quizmoderat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionList(
    val questionId: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: String,
): Parcelable
