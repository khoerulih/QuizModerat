package com.khoerulih.quizmoderat.ui.startquiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.adapter.ListRulesAdapter
import com.khoerulih.quizmoderat.data.RulesStartQuiz
import com.khoerulih.quizmoderat.databinding.ActivityStartQuizBinding
import com.khoerulih.quizmoderat.ui.quiz.QuizActivity

class StartQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartQuizBinding

    private lateinit var startQuizViewModel: StartQuizViewModel
    private lateinit var quizId: String
    private lateinit var quizTitle: String
    private lateinit var quizDescription: String
    private var totalQuestion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startQuizViewModel = ViewModelProvider(this)[StartQuizViewModel::class.java]

        val materiId = intent.getStringExtra(EXTRA_MATERI_ID)

        startQuizViewModel.getQuizRules(materiId!!)

        startQuizViewModel.quizRules.observe(this) { rules ->
            quizId = rules[0].quiz_id
            totalQuestion = rules[0].total_question
            quizTitle = rules[0].quiz_title
            quizDescription = rules[0].quiz_description

            binding.tvTitleStartQuiz.text = rules[0].quiz_title
            binding.tvSubtitleStartQuiz.text = rules[0].quiz_description
            binding.tvTotalQuestion.text = rules[0].total_question.toString()
            binding.tvEstimatedTime.text = getString(R.string.estimated_time, rules[0].estimated_time)
            binding.tvPercentage.text = getString(R.string.percentage, rules[0].percentage)
        }

        binding.btnStartQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra(QuizActivity.EXTRA_QUIZ_ID, quizId)
            intent.putExtra(QuizActivity.EXTRA_TOTAL_QUESTION, totalQuestion)
            intent.putExtra(QuizActivity.EXTRA_QUIZ_TITLE, quizTitle)
            intent.putExtra(QuizActivity.EXTRA_QUIZ_DESCRIPTION, quizDescription)
            intent.putExtra(QuizActivity.EXTRA_MATERI_ID, materiId)
            startActivity(intent)
            finish()
        }

        binding.ivBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        private val TAG = "StartQuizActivity"
        val EXTRA_MATERI_ID = "extra_materi_id"
    }

}