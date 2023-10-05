package com.khoerulih.quizmoderat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Materi(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    var progress: Int = 0,
) : Parcelable
