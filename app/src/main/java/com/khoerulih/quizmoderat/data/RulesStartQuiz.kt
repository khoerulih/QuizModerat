package com.khoerulih.quizmoderat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RulesStartQuiz(
    val quiz_id: String,
    val quiz_title: String,
    val quiz_description: String,
    val total_question: Int,
    val estimated_time: Int,
    val percentage: Int,
) : Parcelable