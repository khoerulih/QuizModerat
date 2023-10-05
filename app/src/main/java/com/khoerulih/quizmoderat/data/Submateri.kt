package com.khoerulih.quizmoderat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Submateri(
    val id: String,
    val noSubmateri: String,
    val title: String,
    val description: String,
    val content: String,
    var isFinished: Boolean? = false
): Parcelable
