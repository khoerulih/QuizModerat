package com.khoerulih.quizmoderat.ui.materi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.adapter.ListMateriAdapter
import com.khoerulih.quizmoderat.data.Materi
import com.khoerulih.quizmoderat.databinding.FragmentMateriBinding

class MateriFragment : Fragment() {

    private var _binding: FragmentMateriBinding? = null

    private val binding get() = _binding!!

    private val list = ArrayList<Materi>()
    private lateinit var materiViewModel: MateriViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        materiViewModel =
            ViewModelProvider(this)[MateriViewModel::class.java]

        auth = Firebase.auth
        _binding = FragmentMateriBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = auth.currentUser

        materiViewModel.listMateri.observe(viewLifecycleOwner){
            val listMateri: MutableList<Materi> = arrayListOf()
            for (materi in it) {
                materiViewModel.getMateriProgress(user!!.uid, materi.id) {progress->
                    materi.progress = progress.first()
                    listMateri.add(materi)
                    showRecyclerList(listMateri.sortedBy { materi -> materi.id })
                }
            }
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

}