package com.khoerulih.quizmoderat.ui.result_quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.MainActivity
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.databinding.ActivityResultQuizBinding

class ResultQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultQuizBinding

    private lateinit var resultQuizViewModel: ResultQuizViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultQuizViewModel = ViewModelProvider(this)[ResultQuizViewModel::class.java]

        auth = Firebase.auth

        val user = auth.currentUser

        val quizScore = intent.getIntExtra(EXTRA_SCORE, 0)
        val quizTitle = intent.getStringExtra(EXTRA_TITLE)
        val quizId = intent.getStringExtra(EXTRA_QUIZ_ID)
        val materiId = intent.getStringExtra(EXTRA_MATERI_ID)

        resultQuizViewModel.getMateriTitle(materiId!!)

        binding.pbResult.progress = quizScore
        binding.tvResultTitle.text = getString(R.string.result_quiz_title, quizTitle)
        binding.tvResultDescription.text = getString(R.string.result_description, quizScore)
        binding.tvResult.text = getString(R.string.percentage, quizScore)

        when (quizScore) {
            in 80..100 -> {
                binding.tvResultGrade.text = getString(R.string.result_grade, "Sangat Baik")
            }

            in 60..79 -> {
                binding.tvResultGrade.text = getString(R.string.result_grade, "Baik")
            }

            in 40..59 -> {
                binding.tvResultGrade.text = getString(R.string.result_grade, "Cukup")
            }

            in 20..39 -> {
                binding.tvResultGrade.text = getString(R.string.result_grade, "Kurang")
            }

            else -> {
                binding.tvResultGrade.text = getString(R.string.result_grade, "Sangat Kurang")
            }
        }

        resultQuizViewModel.materiTitle.observe(this) { materiTitle ->

            resultQuizViewModel.addScoreHistory(
                user!!.uid,
                materiId,
                materiTitle,
                quizId,
                quizScore,
                System.currentTimeMillis()
            )
        }


        binding.btnBackToSubmateri.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }
    }

    companion object {
        private const val TAG = "ResultActivity"
        const val EXTRA_SCORE = "extra_score"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_QUIZ_ID = "extra_quiz_id"
        const val EXTRA_MATERI_ID = "extra_materi_id"
    }
}