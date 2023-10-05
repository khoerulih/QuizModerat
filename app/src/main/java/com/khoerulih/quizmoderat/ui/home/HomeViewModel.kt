package com.khoerulih.quizmoderat.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.data.QuizHistory
import com.khoerulih.quizmoderat.data.Materi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class HomeViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _progressExists = MutableLiveData<Boolean>()
    val progressExists: LiveData<Boolean> = _progressExists

    private val _listMateri = MutableLiveData<List<Materi>>()
    val listMateri: LiveData<List<Materi>> = _listMateri

    private val _listQuizHistory = MutableLiveData<List<QuizHistory>>()
    val listQuizHistory: LiveData<List<QuizHistory>> = _listQuizHistory

    private val _materiProgress = MutableLiveData<Int>()
    val materiProgress : LiveData<Int> = _materiProgress

    init {
        getMateri()
    }

    fun initUserProgress(listMateriId: List<String>, userId: String) {
        db.collection("User_Progress")
            .document(userId)
            .set(hashMapOf("all_progress" to 0))
            .addOnSuccessListener {
                Log.d(TAG, "Data berhasil diupdate")
            }
            .addOnFailureListener {
                Log.w(TAG, "Data gagal diupdate")
            }

        for (materiId in listMateriId) {
            db.collection("User_Progress")
                .document(userId)
                .collection("Materi_Progress")
                .document(materiId)
                .set(hashMapOf("progress" to 0))
        }
    }

    fun checkUserProgress(userId: String) {
        db.collection("User_Progress")
            .document(userId)
            .get()
            .addOnSuccessListener {
                _progressExists.value = it.exists()
            }
    }

    fun getAllProgress(userId: String, allProgressCallback: (Int) -> Unit) {
        db.collection("User_Progress")
            .document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val all_progress = task.result.get("all_progress").toString().toInt()
                    allProgressCallback(all_progress)
                } else {
                    Log.w(TAG, "Failed to get data all progress")
                }
            }
    }

    private fun getMateri() {
        db.collection("Materi")
            .orderBy("no_materi", Query.Direction.ASCENDING)
            .limit(4)
            .get()
            .addOnSuccessListener { result ->
                var listContainer: List<Materi>? = null
                for (document in result) {
                    val id = document.id
                    val title = document.get("title").toString()
                    val description = document.get("description").toString()
                    val icon = document.get("icon").toString()
                    val materi = Materi(id, title, description, icon)
                    listContainer = listContainer?.plus(materi) ?: listOf(materi)
                }
                _listMateri.value = listContainer!!
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    fun getMateriProgress(userId: String, materi_id: String, myCallback: (List<Int>) -> Unit) {
        db.collection("User_Progress")
            .document(userId)
            .collection("Materi_Progress")
            .document(materi_id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val progress = ArrayList<Int>()
                    progress.add(task.result.get("progress").toString().toInt())
                    myCallback(progress)
                } else {
                    Log.w(TAG, "Failed to get data materi progress")
                }
            }
    }

    fun getQuizHistory(userId: String) {
        db.collection("User_Progress")
            .document(userId)
            .collection("Quiz_Result")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(4)
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
        private val TAG = "HomeViewModel"
    }
}