package com.khoerulih.quizmoderat.ui.detail_submateri

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailSubmateriViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _isFinished = MutableLiveData<Boolean>()
    val isFinished: LiveData<Boolean> = _isFinished

    fun setSubmateriStatus(userId: String, materiId: String, submateriId: String) {
        val statusSubmateri = hashMapOf(
            "is_finished" to true
        )
        db.collection("User_Progress")
            .document(userId)
            .collection("Materi_Progress")
            .document(materiId)
            .collection("Submateri_Progress")
            .document(submateriId)
            .set(statusSubmateri)
            .addOnSuccessListener {
                Log.d(TAG, "Success to change submateri status")
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error to change submateri status: ", exception)
            }
    }

    fun getSubmateriStatus(userId: String, materiId: String, submateriId: String) {
        db.collection("User_Progress")
            .document(userId)
            .collection("Materi_Progress")
            .document(materiId)
            .collection("Submateri_Progress")
            .document(submateriId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isFinished.value = task.result.get("is_finished").toString().toBoolean()
                } else {
                    Log.w(TAG, "Error to get submateri status: ", )
                    _isFinished.value = false
                }
            }
    }

    companion object {
        private const val TAG = "DetailSubmateriViewModel"
    }
}