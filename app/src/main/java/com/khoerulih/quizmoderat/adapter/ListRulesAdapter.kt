package com.khoerulih.quizmoderat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khoerulih.quizmoderat.data.RulesStartQuiz
import com.khoerulih.quizmoderat.databinding.ItemRowStartQuizBinding

class ListRulesAdapter(private val listRules: ArrayList<RulesStartQuiz>) :
    RecyclerView.Adapter<ListRulesAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ListViewHolder {
        val itemBinding =
            ItemRowStartQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListRulesAdapter.ListViewHolder, position: Int) {
        val rules: RulesStartQuiz = listRules[position]
        holder.bind(rules)
    }

    override fun getItemCount(): Int = listRules.size

    class ListViewHolder(private val itemBinding: ItemRowStartQuizBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(rules: RulesStartQuiz) {
        }
    }
}