package com.khoerulih.quizmoderat.ui.forget_password

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khoerulih.quizmoderat.ui.auth.LoginActivity
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.databinding.FragmentEmailValidationBinding


class EmailValidationFragment : Fragment() {

    private lateinit var binding: FragmentEmailValidationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmailValidationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        binding.btnChangePassword.setOnClickListener {
            val changePasswordFragment = ChangePasswordFragment()
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(
                    R.id.frame_container,
                    changePasswordFragment,
                    ChangePasswordFragment::class.java.simpleName
                )
                addToBackStack(null)
                commit()
            }
        }
    }
}