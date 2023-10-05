package com.khoerulih.quizmoderat.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel: ViewModel() {

    private val db = Firebase.firestore

    fun initUserProgress(listMateriId: List<String>, userId: String){
        val data:HashMap<String, Int> = HashMap()
        for (materiId in listMateriId){
            data.put(materiId, 0)
        }
        db.collection("User_Progress")
            .document(userId)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {

            }
    }
}