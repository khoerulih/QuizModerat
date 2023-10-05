package com.khoerulih.quizmoderat.ui.submateri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.MainActivity
import com.khoerulih.quizmoderat.adapter.ListSubmateriAdapter
import com.khoerulih.quizmoderat.data.Submateri
import com.khoerulih.quizmoderat.databinding.ActivitySubmateriBinding
import com.khoerulih.quizmoderat.ui.startquiz.StartQuizActivity
import com.khoerulih.quizmoderat.ui.startquiz.StartQuizViewModel

class SubmateriActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmateriBinding
    private lateinit var submateriViewModel: SubmateriViewModel
    private lateinit var startQuizViewModel: StartQuizViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubmateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val user = auth.currentUser

        submateriViewModel = ViewModelProvider(this)[SubmateriViewModel::class.java]
        startQuizViewModel = ViewModelProvider(this)[StartQuizViewModel::class.java]

        val materiId = intent.getStringExtra(EXTRA_MATERI_ID)
        val materiTitle = intent.getStringExtra(EXTRA_MATERI_TITLE)
        val materiDescription = intent.getStringExtra(EXTRA_MATERI_DESCRIPTION)

        submateriViewModel.getSubmateri(materiId!!)
        submateriViewModel.listSubmateri.observe(this) { listSubmateri ->
            showRecyclerList(
                listSubmateri.sortedBy { submateri -> submateri.noSubmateri },
                materiId
            )
        }

        submateriViewModel.getUserBestScore(user!!.uid, materiId)

        submateriViewModel.listScore.observe(this) { score ->
            val newList = score.sortedDescending()
            Log.d(TAG, "${score}")
            if (newList.isEmpty()) {
                binding.tvBestScore.text = "-"
            } else {
                binding.tvBestScore.text = newList[0].toString()
            }
        }

        submateriViewModel.getMateriProgress(user.uid, materiId)
        submateriViewModel.countFinishedSubmateri(user.uid, materiId)
        submateriViewModel.countSubmateri(materiId)

        submateriViewModel.currentMateriProgress.observe(this) { currentMateriProgress ->
            submateriViewModel.numberFinishedSubmateri.observe(this) { countMateriFinished ->
                submateriViewModel.totalSubmateri.observe(this) { totalSubmateri ->
                    val userProgress = (countMateriFinished * 100) / totalSubmateri
                    if (userProgress != currentMateriProgress){
                        submateriViewModel.updateMateriProgress(user.uid, materiId, userProgress)
                    }
                }
            }
        }

        binding.tvTitleSubmateri.text = materiTitle
        binding.tvSubtitleSubmateri.text = materiDescription

        startQuizViewModel.getQuizRules(materiId)

        startQuizViewModel.quizRules.observe(this) { rules ->
            binding.tvQuiz.text = rules[0].quiz_title
            binding.tvDescQuiz.text = rules[0].quiz_description
        }

        binding.cvQuiz.setOnClickListener {
            val intent = Intent(this, StartQuizActivity::class.java)
            intent.putExtra(StartQuizActivity.EXTRA_MATERI_ID, materiId)
            startActivity(intent)
        }

        binding.ivBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showRecyclerList(listSubmateri: List<Submateri>, materiId: String) {
        binding.rvSubmateri.layoutManager = LinearLayoutManager(this)
        val listSubmateriAdapter = ListSubmateriAdapter(listSubmateri, materiId)
        binding.rvSubmateri.adapter = listSubmateriAdapter
    }

    companion object {
        const val TAG = "SubmateriActivity"
        const val EXTRA_MATERI_ID = "extra_materi_id"
        const val EXTRA_MATERI_TITLE = "extra_materi_title"
        const val EXTRA_MATERI_DESCRIPTION = "extra_materi_descrpiption"
    }
}