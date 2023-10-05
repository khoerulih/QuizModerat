package com.khoerulih.quizmoderat.ui.materi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.data.Materi
import com.khoerulih.quizmoderat.ui.home.HomeViewModel

class MateriViewModel : ViewModel() {

    private val db = Firebase.firestore

    private val _listMateri = MutableLiveData<List<Materi>>()
    val listMateri: LiveData<List<Materi>> = _listMateri

    private val _listMateriId = MutableLiveData<List<String>>()
    val listMateriId: LiveData<List<String>> = _listMateriId

    init {
        getMateri()
        getListMateriId()
    }

    private fun getMateri() {
        db.collection("Materi")
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

    fun getListMateriId(){
        db.collection("Materi")
            .get()
            .addOnSuccessListener { result ->
                var materiId: List<String>? = null
                for (document in result) {
                    materiId = materiId?.plus(document.id) ?: listOf(document.id)
                }
                _listMateriId.value = materiId!!
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
                    Log.d(TAG, "${progress.first()}")
                    myCallback(progress)
                } else {
                    Log.w(TAG, "Failed to get data materi progress")
                }
            }
    }

    companion object {
        private val TAG = "MateriViewModel"
    }
}