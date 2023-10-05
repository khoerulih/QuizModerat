package com.khoerulih.quizmoderat.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.adapter.ListQuizHistoryAdapter
import com.khoerulih.quizmoderat.adapter.ListMateriAdapter
import com.khoerulih.quizmoderat.data.QuizHistory
import com.khoerulih.quizmoderat.data.Materi
import com.khoerulih.quizmoderat.databinding.FragmentHomeBinding
import com.khoerulih.quizmoderat.ui.materi.MateriViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var materiViewModel: MateriViewModel
    private lateinit var homeViewModel: HomeViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        materiViewModel = ViewModelProvider(this)[MateriViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser
        val firstname = user?.displayName?.substring(0, user.displayName?.indexOf(" ")!!)
        binding.tvHomeTitle.text = getString(R.string.greeting, firstname)

        homeViewModel.checkUserProgress(user!!.uid)
        homeViewModel.getQuizHistory(user.uid)

        homeViewModel.progressExists.observe(viewLifecycleOwner) { exists ->
            if (exists) {
                getUserProgress(user.uid)
                homeViewModel.listMateri.observe(viewLifecycleOwner) {
                    val listMateri: MutableList<Materi> = arrayListOf()
                    for (materi in it) {
                        homeViewModel.getMateriProgress(user.uid, materi.id) { progress ->
                            materi.progress = progress.first()
                            listMateri.add(materi)
                            showRecyclerList(listMateri.sortedBy { materi -> materi.id })
                        }
                    }
                }
            } else {
                materiViewModel.listMateriId.observe(viewLifecycleOwner) { listMateri ->
                    homeViewModel.initUserProgress(listMateri, user.uid)
                }
                getUserProgress(user.uid)
                homeViewModel.listMateri.observe(viewLifecycleOwner) {
                    val listMateri: MutableList<Materi> = arrayListOf()
                    for (materi in it) {
                        homeViewModel.getMateriProgress(user.uid, materi.id) { progress ->
                            materi.progress = progress.first()
                            listMateri.add(materi)
                            showRecyclerList(listMateri.sortedBy { materi -> materi.id })
                        }
                    }
                }
            }
        }

        homeViewModel.listQuizHistory.observe(viewLifecycleOwner) {
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

        binding.tvNavigateToMateri.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.navigation_list_materi)
        }

        binding.tvNavigateToHistory.setOnClickListener {
            findNavController().popBackStack()
            findNavController().navigate(R.id.navigation_history)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerList(listMateri: List<Materi>) {
        binding.rvListMateri.layoutManager = GridLayoutManager(context, 2)
        val listMateriAdapter = ListMateriAdapter(listMateri)
        binding.rvListMateri.adapter = listMateriAdapter
    }

    private fun showHistoryRecyclerList(listQuizHistory: List<QuizHistory>) {
        val layoutManager = LinearLayoutManager(context)
        binding.rvHistory.layoutManager = layoutManager
        val listQuizHistoryAdapter = ListQuizHistoryAdapter(listQuizHistory)
        binding.rvHistory.adapter = listQuizHistoryAdapter
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvHistory.addItemDecoration(itemDecoration)
    }

    private fun getUserProgress(userId: String) {
        homeViewModel.getAllProgress(userId) { result ->
            binding.tvProgress.text = context?.getString(R.string.all_progress, result)
            binding.pbAllProgress.progress = result
            if (result > 75) {
                binding.pbAllProgress.setIndicatorColor(Color.parseColor("#60BF8B"))
                binding.tvStatus.text = context?.getString(R.string.overall_status, "Baik")
                binding.tvStatusSubtitle.text =
                    context?.getString(R.string.status_subtitle_baik)
            } else if (result > 50) {
                binding.pbAllProgress.setIndicatorColor(Color.parseColor("#CACD50"))
                binding.tvStatus.text = context?.getString(R.string.overall_status, "Cukup")
                binding.tvStatusSubtitle.text =
                    context?.getString(R.string.status_subtitle_cukup)
            } else {
                binding.pbAllProgress.setIndicatorColor(Color.parseColor("#E35858"))
                binding.tvStatus.text =
                    context?.getString(R.string.overall_status, "Kurang")
                binding.tvStatusSubtitle.text =
                    context?.getString(R.string.status_subtitle_kurang)
            }
        }
    }

    companion object {
        private val TAG = "HomeFragment"
    }

}