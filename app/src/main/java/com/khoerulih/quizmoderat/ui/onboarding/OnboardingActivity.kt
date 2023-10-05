package com.khoerulih.quizmoderat.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.khoerulih.quizmoderat.MainActivity
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.SettingPreferences
import com.khoerulih.quizmoderat.dataStore
import com.khoerulih.quizmoderat.databinding.ActivityOnboardingBinding
import com.khoerulih.quizmoderat.ui.ViewModelFactory
import com.khoerulih.quizmoderat.ui.auth.LoginActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val onboardingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            OnboardingViewModel::class.java
        )

        onboardingViewModel.getOnboardingStatusKey().observe(this){ status ->
            if(status){
                goToLoginActivity()
            }
        }

        binding.btnMulai.setOnClickListener {
            onboardingViewModel.saveOnboardingStatusKey(true)
            goToLoginActivity()
        }

        binding.tvSkip.setOnClickListener {
            onboardingViewModel.saveOnboardingStatusKey(true)
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}