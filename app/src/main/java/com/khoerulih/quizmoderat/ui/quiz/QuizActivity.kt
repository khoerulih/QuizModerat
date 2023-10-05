package com.khoerulih.quizmoderat.ui.quiz

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.khoerulih.quizmoderat.MainActivity
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.data.QuestionList
import com.khoerulih.quizmoderat.databinding.ActivityQuizBinding
import com.khoerulih.quizmoderat.ui.auth.LoginActivity
import com.khoerulih.quizmoderat.ui.home.HomeFragment
import com.khoerulih.quizmoderat.ui.result_quiz.ResultQuizActivity

class QuizActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityQuizBinding

    private var mSelectedOptionPosition: Int = 0

    private lateinit var quizViewModel: QuizViewModel

    private var userAnswer: String = ""
    private var realAnswer: String = ""

    private var showedQuestion: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizViewModel = ViewModelProvider(this)[QuizViewModel::class.java]

        val quizId = intent.getStringExtra(EXTRA_QUIZ_ID)
        val quizTitle = intent.getStringExtra(EXTRA_QUIZ_TITLE)
        val quizDescription = intent.getStringExtra(EXTRA_QUIZ_DESCRIPTION)
        val totalQuestion = intent.getIntExtra(EXTRA_TOTAL_QUESTION, 0)
        val materiId = intent.getStringExtra(EXTRA_MATERI_ID)

        var questionCount = 1
        var score = 0

        initOption()

        quizViewModel.getQuestion(quizId!!)

        getRandomQuestion(totalQuestion)

        binding.tvQuestionCount.text =
            getString(R.string.question_count, questionCount, totalQuestion)
        binding.tvQuizTitle.text = quizTitle
        binding.tvQuizDescription.text = quizDescription

        binding.let {
            it.tvOptionOne.setOnClickListener(this)
            it.tvOptionTwo.setOnClickListener(this)
            it.tvOptionThree.setOnClickListener(this)
            it.tvOptionFour.setOnClickListener(this)
        }

        binding.btnNext.setOnClickListener {
            if (questionCount == totalQuestion) {
                val intent = Intent(this, ResultQuizActivity::class.java)
                intent.putExtra(ResultQuizActivity.EXTRA_SCORE, score)
                intent.putExtra(ResultQuizActivity.EXTRA_TITLE, quizTitle)
                intent.putExtra(ResultQuizActivity.EXTRA_QUIZ_ID, quizId)
                intent.putExtra(ResultQuizActivity.EXTRA_MATERI_ID, materiId)
                startActivity(intent)
                finish()
            } else {
                if (userAnswer == realAnswer) {
                    score += 100 / totalQuestion
                }
                getRandomQuestion(totalQuestion)
                questionCount++
                binding.tvQuestionCount.text =
                    getString(R.string.question_count, questionCount, totalQuestion)
                initOption()
            }

            if (questionCount > 1) {
                binding.ivBack.visibility = View.GONE
            } else {
                binding.ivBack.visibility = View.VISIBLE
            }
        }



        binding.ivBack.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Keluar dari quiz")
                .setMessage("Apakah anda yakin untuk membatalkan quiz?")
                .setPositiveButton("Iya") { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Tidak") { _, _ ->
                    // Do Nothing
                }
                .show()
        }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_option_one -> {
                selectedOption(binding.tvOptionOne, 0)
            }

            R.id.tv_option_two -> {
                selectedOption(binding.tvOptionTwo, 1)
            }

            R.id.tv_option_three -> {
                selectedOption(binding.tvOptionThree, 2)
            }

            R.id.tv_option_four -> {
                selectedOption(binding.tvOptionFour, 3)
            }
        }
    }

    private fun initOption() {
        val options = ArrayList<TextView>()
        options.add(0, binding.tvOptionOne)
        options.add(1, binding.tvOptionTwo)
        options.add(2, binding.tvOptionThree)
        options.add(3, binding.tvOptionFour)

        for (option in options) {
            option.setTextColor(ContextCompat.getColor(this, R.color.soft_green))
            option.background = ContextCompat.getDrawable(this, R.drawable.bg_option_default)
        }
    }

    private fun selectedOption(tv: TextView, selectedOption: Int) {
        initOption()

        mSelectedOptionPosition = selectedOption

        tv.setTextColor(ContextCompat.getColor(this, R.color.green))
        tv.background = ContextCompat.getDrawable(this, R.drawable.bg_option_selected)
        userAnswer = tv.text.toString()
    }

    private fun getRandomQuestion(totalQuestion: Int) {
        quizViewModel.listQuestion.observe(this) { result ->
            val randomIndex = (0 until totalQuestion).random()
            val question = result[randomIndex]
            if (showedQuestion.contains(question.questionId)) {
                getRandomQuestion(totalQuestion)
            } else {
                showedQuestion.add(question.questionId)
                showQuestion(question)
            }
        }
    }

    private fun showQuestion(question: QuestionList) {
        binding.tvPertanyaan.text = question.question
        binding.tvOptionOne.text = question.optionA
        binding.tvOptionTwo.text = question.optionB
        binding.tvOptionThree.text = question.optionC
        binding.tvOptionFour.text = question.optionD
        realAnswer = question.answer
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        AlertDialog.Builder(this)
            .setTitle("Keluar dari quiz")
            .setMessage("Apakah anda yakin untuk membatalkan quiz?")
            .setPositiveButton("Iya") { _, _ ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Tidak") { _, _ ->
                // Do Nothing
            }
            .show()
    }

    companion object {
        private val TAG = "QuizActivity"
        const val EXTRA_QUIZ_ID = "extra_quiz_id"
        const val EXTRA_QUIZ_TITLE = "extra_quiz_title"
        const val EXTRA_QUIZ_DESCRIPTION = "extra_quiz_description"
        const val EXTRA_TOTAL_QUESTION = "extra_total_question"
        const val EXTRA_MATERI_ID = "extra_materi_id"
    }

}