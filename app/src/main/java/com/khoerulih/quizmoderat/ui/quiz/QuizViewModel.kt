package com.khoerulih.quizmoderat.ui.quiz

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.data.QuestionList

class QuizViewModel: ViewModel() {

    private val db = Firebase.firestore

    private val _listQuestion = MutableLiveData<List<QuestionList>>()

    val listQuestion: LiveData<List<QuestionList>> = _listQuestion

    @SuppressLint("NullSafeMutableLiveData")
    fun getQuestion(quizId: String){
        db.collection("Quiz")
            .document(quizId)
            .collection("question_list")
            .get()
            .addOnSuccessListener { result ->
                var questionContainer: List<QuestionList>? = null
                for (document in result) {
                    val questionId = document.id
                    val question = document.get("question").toString()
                    val optionA = document.get("option_a").toString()
                    val optionB = document.get("option_b").toString()
                    val optionC = document.get("option_c").toString()
                    val optionD = document.get("option_d").toString()
                    val answer = document.get("answer").toString()
                    val questionItem = QuestionList(questionId, question, optionA, optionB, optionC, optionD, answer)
                    questionContainer = questionContainer?.plus(questionItem) ?: listOf(questionItem)
                }
                if (questionContainer != null) {
                    _listQuestion.value = questionContainer
                }
            }
    }
}