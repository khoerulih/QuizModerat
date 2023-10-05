package com.khoerulih.quizmoderat.ui.quiz_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.adapter.ListQuizHistoryAdapter
import com.khoerulih.quizmoderat.data.QuizHistory
import com.khoerulih.quizmoderat.databinding.FragmentHistoryBinding

class QuizHistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var quizHistoryViewModel: QuizHistoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        quizHistoryViewModel =
            ViewModelProvider(this)[QuizHistoryViewModel::class.java]
        auth = Firebase.auth

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser
        quizHistoryViewModel.getQuizHistory(user!!.uid)

        quizHistoryViewModel.listQuizHistory.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.tvEmptyHistory.visibility = View.VISIBLE
            } else {
                binding.tvEmptyHistory.visibility = View.INVISIBLE
                val listQuizHistory: MutableList<QuizHistory> = arrayListOf()
                for (quizHistory in it) {
                    listQuizHistory.add(quizHistory)
                    showHistoryRecyclerList(listQuizHistory.sortedByDescending { item -> item.timestamp })
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showHistoryRecyclerList(listQuizHistory: List<QuizHistory>) {
        val layoutManager = LinearLayoutManager(context)
        binding.rvHistory.layoutManager = layoutManager
        val listQuizHistoryAdapter = ListQuizHistoryAdapter(listQuizHistory)
        binding.rvHistory.adapter = listQuizHistoryAdapter
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)
    }
}