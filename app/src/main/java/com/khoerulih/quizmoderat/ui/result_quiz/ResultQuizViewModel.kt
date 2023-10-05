package com.khoerulih.quizmoderat.ui.result_quiz

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.ui.home.HomeViewModel

class ResultQuizViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _materiTitle = MutableLiveData<String>()
    val materiTitle: LiveData<String> = _materiTitle

    fun addScoreHistory(
        userId: String,
        materiId: String?,
        materiTitle: String,
        quizId: String?,
        score: Int,
        timestamp: Long,
    ) {
        val history = hashMapOf(
            "materi_id" to materiId,
            "materi_title" to materiTitle,
            "quiz_id" to quizId,
            "score" to score,
            "timestamp" to timestamp
        )
        db.collection("User_Progress")
            .document(userId)
            .collection("Quiz_Result")
            .document()
            .set(history)
            .addOnSuccessListener {
                Log.d(TAG, "Success to add user history")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error to add user history: ", exception)
            }
    }

    fun getMateriTitle(materiId: String){
        db.collection("Materi")
            .document(materiId)
            .get()
            .addOnCompleteListener {task ->
                if(task.isSuccessful){
                    val title = task.result.get("title").toString()
                    _materiTitle.value = title
                }
                else {
                    Log.w(TAG, "Failed to get data")
                }
            }
    }

    companion object {
        private const val TAG = "ResultQuizViewModel"
    }
}