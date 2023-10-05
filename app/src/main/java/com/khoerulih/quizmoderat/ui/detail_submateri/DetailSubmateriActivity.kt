package com.khoerulih.quizmoderat.ui.detail_submateri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.databinding.ActivityDetailSubmateriBinding
import com.khoerulih.quizmoderat.databinding.ActivitySubmateriBinding

class DetailSubmateriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailSubmateriBinding

    private lateinit var detailSubmateriViewModel: DetailSubmateriViewModel

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSubmateriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        detailSubmateriViewModel = ViewModelProvider(this)[DetailSubmateriViewModel::class.java]

        val user = auth.currentUser

        val submateriId = intent.getStringExtra(EXTRA_SUBMATERI_ID)
        val title = intent.getStringExtra(EXTRA_SUBMATERI_TITLE)
        val description = intent.getStringExtra(EXTRA_SUBMATERI_DESCRIPTION)
        val content = intent.getStringExtra(EXTRA_SUBMATERI_CONTENT)
        val materiId = intent.getStringExtra(EXTRA_MATERI_ID)

        detailSubmateriViewModel.getSubmateriStatus(user!!.uid, materiId.toString(), submateriId.toString())

        detailSubmateriViewModel.isFinished.observe(this){isFinished ->
            if (isFinished){
                binding.btnMarkAsDone.text = "Materi ini sudah selesai dibaca"
                binding.btnMarkAsDone.isEnabled = false
            } else {
                binding.btnMarkAsDone.text = "Tandai sudah selesai"
                binding.btnMarkAsDone.isEnabled = true
            }
        }

        binding.tvTitleSubmateri.text = title
        binding.tvSubtitleSubmateri.text = description
        binding.tvContent.text = content

        binding.ivBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.btnMarkAsDone.setOnClickListener {
            detailSubmateriViewModel.setSubmateriStatus(
                user.uid,
                materiId.toString(),
                submateriId.toString()
            )

            onBackPressedDispatcher.onBackPressed()
        }
    }

    companion object {
        private const val TAG = "DetailSubmateriActivity"
        const val EXTRA_SUBMATERI_ID = "extra_submateri_id"
        const val EXTRA_SUBMATERI_TITLE = "extra_submateri_title"
        const val EXTRA_SUBMATERI_DESCRIPTION = "extra_submateri_description"
        const val EXTRA_SUBMATERI_CONTENT = "extra_submateri_content"
        const val EXTRA_MATERI_ID = "extra_materi_id"
    }
}