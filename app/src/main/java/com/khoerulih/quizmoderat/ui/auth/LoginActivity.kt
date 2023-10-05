package com.khoerulih.quizmoderat.ui.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.MainActivity
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.SettingPreferences
import com.khoerulih.quizmoderat.authErrors
import com.khoerulih.quizmoderat.dataStore
import com.khoerulih.quizmoderat.databinding.ActivityLoginBinding
import com.khoerulih.quizmoderat.ui.ViewModelFactory
import com.khoerulih.quizmoderat.ui.forget_password.ForgetPasswordActivity
import com.khoerulih.quizmoderat.ui.onboarding.OnboardingActivity
import com.khoerulih.quizmoderat.ui.onboarding.OnboardingViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val pref = SettingPreferences.getInstance(application.dataStore)
        val onboardingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            OnboardingViewModel::class.java
        )

        onboardingViewModel.getOnboardingStatusKey().observe(this){ status ->
            if(!status){
                val intent = Intent(this, OnboardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        binding.btnSignIn.setOnClickListener {
            val email = binding.edtEmailValue.text
            val password = binding.edtPasswordValue.text

            if(email.isNullOrEmpty()) {
                binding.edtEmailValue.error = "Email tidak boleh kosong!!"
            }

            if(password.isNullOrEmpty()) {
                binding.edtPasswordValue.error = "Password tidak boleh kosong!!"
            }

            if (email!!.isNotEmpty() && password!!.isNotEmpty()){
                binding.clLoading.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener(this) { task ->
                        binding.clLoading.visibility = View.INVISIBLE
                        if (task.isSuccessful){
                            Log.d(TAG, "signInWithEmail:success")
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            val errorCode = (task.exception as FirebaseAuthException).errorCode
                            val errorMessage = authErrors[errorCode] ?: R.string.error_login_default
                            Snackbar.make(
                                binding.root,
                                errorMessage,
                                Snackbar.LENGTH_SHORT,
                            ).setBackgroundTint(Color.RED).show()
                            updateUI(null)
                        }
                    }
            }
        }

        binding.tvToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvForgetPassword.setOnClickListener {
            val intent = Intent(this, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        private val TAG = "LoginActivity"
    }
}