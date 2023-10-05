package com.khoerulih.quizmoderat.ui.auth

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.MainActivity
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.authErrors
import com.khoerulih.quizmoderat.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnSignUp.setOnClickListener {
            val email = binding.edtEmailValue.text
            val password = binding.edtPasswordValue.text
            val firstname = binding.edtFirstnameValue.text
            val lastname = binding.edtLastnameValue.text

            if (firstname.isNullOrEmpty()) {
                binding.edtFirstnameValue.error = "Nama Depan tidak boleh kosong!!"
            }

            if (lastname.isNullOrEmpty()) {
                binding.edtLastnameValue.error = "Nama Belakang tidak boleh kosong!!"
            }

            if(email.isNullOrEmpty()) {
                binding.edtEmailValue.error = "Email tidak boleh kosong!!"
            }

            if(password.isNullOrEmpty()) {
                binding.edtPasswordValue.error = "Password tidak boleh kosong!!"
            }

            if (email!!.isNotEmpty() && password!!.isNotEmpty() && firstname!!.isNotEmpty() && lastname!!.isNotEmpty()){
                binding.clLoading.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                    .addOnCompleteListener(this) { task ->
                        binding.clLoading.visibility = View.INVISIBLE
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(
                                baseContext,
                                "Registrasi Berhasil.",
                                Toast.LENGTH_SHORT,
                            ).show()

                            val user = auth.currentUser

                            val fullname = "$firstname $lastname"
                            val profileUpdates = userProfileChangeRequest {
                                displayName = fullname
                            }

                            user!!.updateProfile(profileUpdates)
                                .addOnCompleteListener(this) { profileTask ->
                                    if (profileTask.isSuccessful) {
                                        Log.d(TAG, "User profile name updated")
                                        updateUI(user)
                                    }
                                }
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            val errorCode = (task.exception as FirebaseAuthException).errorCode
                            val errorMessage = authErrors[errorCode] ?: R.string.error_register_default
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

        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}