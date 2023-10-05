package com.khoerulih.quizmoderat.ui.about

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.khoerulih.quizmoderat.MainActivity
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.databinding.ActivityAboutBinding
import com.khoerulih.quizmoderat.ui.forget_password.ForgetPasswordActivity
import com.khoerulih.quizmoderat.ui.profile.ProfileFragment

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}