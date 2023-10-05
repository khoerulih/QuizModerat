package com.khoerulih.quizmoderat.ui.submateri

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.data.Submateri

class SubmateriViewModel : ViewModel() {
    private val db = Firebase.firestore

    private val _listSubmateri = MutableLiveData<List<Submateri>>()
    val listSubmateri: LiveData<List<Submateri>> = _listSubmateri

    private val _listScore = MutableLiveData<List<Int>>()
    val listScore: LiveData<List<Int>> = _listScore

    private val _currentMateriProgress = MutableLiveData<Int>()
    val currentMateriProgress: LiveData<Int> = _currentMateriProgress

    private val _numberFinishedSubmateri = MutableLiveData<Int>()
    val numberFinishedSubmateri: LiveData<Int> = _numberFinishedSubmateri

    private val _totalSubmateri = MutableLiveData<Int>()
    val totalSubmateri : LiveData<Int> = _totalSubmateri

    @SuppressLint("NullSafeMutableLiveData")
    fun getSubmateri(materiId: String) {
        db.collection("Submateri")
            .whereEqualTo("materi_id", materiId)
            .get()
            .addOnSuccessListener { result ->
                var submateriContainer: List<Submateri>? = null
                for (document in result) {
                    val id = document.id
                    val noSubmateri = document.get("no_submateri").toString()
                    val title = document.get("title").toString()
                    val description = document.get("description").toString()
                    val content = document.get("content").toString()
                    val submateri = Submateri(id, noSubmateri, title, description, content)
                    submateriContainer = submateriContainer?.plus(submateri) ?: listOf(submateri)
                }
                if (submateriContainer != null) {
                    _listSubmateri.value = submateriContainer
                }
            }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun getUserBestScore(userId: String, materiId: String) {
        db.collection("User_Progress")
            .document(userId)
            .collection("Quiz_Result")
            .whereEqualTo("materi_id", materiId)
            .get()
            .addOnSuccessListener { result ->
                var scoreContainer: List<Int>? = null
                for (document in result){
                    val score = document.get("score").toString().toInt()
                    scoreContainer = scoreContainer?.plus(score) ?: listOf(score)
                }
                if (scoreContainer != null) {
                    _listScore.value = scoreContainer
                } else {
                    _listScore.value = listOf()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Failed to get user best score: ", exception)
            }
    }

    fun countSubmateri(materiId: String) {
        db.collection("Submateri")
            .whereEqualTo("materi_id", materiId)
            .count()
            .get(AggregateSource.SERVER)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _totalSubmateri.value = task.result.count.toInt()
                } else {
                    Log.w(TAG, "Failed to count submateri")
                }
            }
    }

    fun getMateriProgress(userId: String, materiId: String){
        db.collection("User_Progress")
            .document(userId)
            .collection("Materi_Progress")
            .document(materiId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _currentMateriProgress.value = task.result.get("progress").toString().toInt()
                } else {
                    Log.w(TAG, "Failed to get Materi Progress")
                }
            }
    }

    fun countFinishedSubmateri(userId: String, materiId: String){
        db.collection("User_Progress")
            .document(userId)
            .collection("Materi_Progress")
            .document(materiId)
            .collection("Submateri_Progress")
            .count()
            .get(AggregateSource.SERVER)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    _numberFinishedSubmateri.value = task.result.count.toInt()
                } else {
                    Log.w(TAG, "Failed to get count submateri Progress")
                }
            }
    }

    fun updateMateriProgress(userId:String, materiId: String, progress: Int){
        val materiProgress = hashMapOf(
            "progress" to progress
        )
        db.collection("User_Progress")
            .document(userId)
            .collection("Materi_Progress")
            .document(materiId)
            .set(materiProgress)
            .addOnSuccessListener {
                Log.d(TAG, "Update Materi Progress Success")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Update Materi Progress Failed: ", exception)
            }
    }

    companion object {
        private const val TAG = "SubmateriViewModel"
    }

}