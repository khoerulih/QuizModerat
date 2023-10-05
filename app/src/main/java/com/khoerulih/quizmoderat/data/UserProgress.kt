package com.khoerulih.quizmoderat.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserProgress(
    val all_progress: Int,
):Parcelable
