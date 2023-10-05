package com.khoerulih.quizmoderat.ui.startquiz

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.data.RulesStartQuiz

class StartQuizViewModel: ViewModel() {

    private val db = Firebase.firestore

    private val _quizRules = MutableLiveData<List<RulesStartQuiz>>()
    val quizRules : LiveData<List<RulesStartQuiz>> = _quizRules

    @SuppressLint("NullSafeMutableLiveData")
    fun getQuizRules(materiId: String){
        db.collection("Quiz")
            .whereEqualTo("materi_id", materiId)
            .get()
            .addOnSuccessListener {result ->
                var rulesContainer : List<RulesStartQuiz>? = null
                for (document in result){
                    val quizId = document.id
                    val quizTitle = document.get("title").toString()
                    val quizDescription = document.get("description").toString()
                    val totalQuestion = document.get("total_question").toString().toInt()
                    val estimatedTime = document.get("estimated_time").toString().toInt()
                    val percentage = document.get("percentage").toString().toInt()
                    val rules = RulesStartQuiz(quizId, quizTitle, quizDescription, totalQuestion, estimatedTime, percentage)
                    rulesContainer = rulesContainer?.plus(rules) ?: listOf(rules)
                }
                if (rulesContainer != null) {
                    _quizRules.value = rulesContainer
                }
            }
    }

    companion object {
        private const val TAG = "StartQuizViewModel"
    }
}