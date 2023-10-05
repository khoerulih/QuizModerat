package com.khoerulih.quizmoderat.ui.forget_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.databinding.ActivityForgetPasswordBinding
import com.khoerulih.quizmoderat.ui.forget_password.EmailValidationFragment

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val emailValidationFragment = EmailValidationFragment()
        val fragment =
            fragmentManager.findFragmentByTag(EmailValidationFragment::class.java.simpleName)

        if (fragment !is EmailValidationFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, emailValidationFragment, EmailValidationFragment::class.java.simpleName)
                .commit()
        }
    }
}