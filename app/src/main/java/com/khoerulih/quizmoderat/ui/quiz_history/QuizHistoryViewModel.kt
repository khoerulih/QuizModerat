package com.khoerulih.quizmoderat.ui.quiz_history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.data.QuizHistory
import com.khoerulih.quizmoderat.ui.home.HomeViewModel

class QuizHistoryViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _listQuizHistory = MutableLiveData<List<QuizHistory>>()
    val listQuizHistory: LiveData<List<QuizHistory>> = _listQuizHistory

    fun getQuizHistory(userId: String) {
        db.collection("User_Progress")
            .document(userId)
            .collection("Quiz_Result")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                var listQuizHistoryContainer: List<QuizHistory>? = null

                for (document in result) {
                    val id = document.id
                    val materiId = document.get("materi_id").toString()
                    val materiTitle = document.get("materi_title").toString()
                    val quizId = document.get("quiz_id").toString()
                    val score = document.get("score").toString().toInt()
                    val timestamp = document.get("timestamp").toString().toLong()
                    val quizHistory =
                        QuizHistory(id, materiId, materiTitle, quizId, score, timestamp)
                    listQuizHistoryContainer =
                        listQuizHistoryContainer?.plus(quizHistory) ?: listOf(quizHistory)
                }
                if (listQuizHistoryContainer != null) {

                    _listQuizHistory.value = listQuizHistoryContainer!!
                } else {
                    _listQuizHistory.value = listOf()
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Failed to get quiz history: ", exception)
            }
    }

    companion object {
        private const val TAG = "QuizHistoryViewModel"
    }
}