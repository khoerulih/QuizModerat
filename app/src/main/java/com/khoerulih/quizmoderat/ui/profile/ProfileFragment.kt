package com.khoerulih.quizmoderat.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.databinding.FragmentProfileBinding
import com.khoerulih.quizmoderat.ui.about.AboutActivity
import com.khoerulih.quizmoderat.ui.auth.LoginActivity
import com.khoerulih.quizmoderat.ui.forget_password.ForgetPasswordActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser

        binding.tvFullname.text = user?.displayName
        binding.tvEmail.text = user?.email

        binding.btnChangePassword.setOnClickListener {
            val intent = Intent(activity, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.btnAbout.setOnClickListener {
            val intent = Intent(activity, AboutActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignOut.setOnClickListener {

            AlertDialog.Builder(activity)
                .setTitle("Keluar")
                .setMessage("Apakah anda yakin?")
                .setPositiveButton("Iya"){ _, _ ->
                    auth.signOut()
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finishAffinity()
                }
                .setNegativeButton("Tidak"){ _, _ ->
                    // Do Nothing
                }
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}