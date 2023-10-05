package com.khoerulih.quizmoderat.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khoerulih.quizmoderat.data.Submateri
import com.khoerulih.quizmoderat.databinding.ItemRowSubmateriBinding
import com.khoerulih.quizmoderat.ui.detail_submateri.DetailSubmateriActivity

class ListSubmateriAdapter(private val listSubmateri: List<Submateri>, private val materiId: String) :
    RecyclerView.Adapter<ListSubmateriAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemRowSubmateriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = listSubmateri.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val submateri: Submateri = listSubmateri[position]
        holder.bind(submateri, materiId)
    }

    class ListViewHolder(private val itemBinding: ItemRowSubmateriBinding):
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(submateri: Submateri, materiId: String) {
            itemBinding.tvNoSubmateri.text = submateri.noSubmateri
            itemBinding.tvTitleSubmateri.text = submateri.title
            itemBinding.tvDescSubmateri.text = submateri.description

            itemBinding.cvListSubmateri.setOnClickListener {
                val intent = Intent(itemView.context, DetailSubmateriActivity::class.java)
                intent.putExtra(DetailSubmateriActivity.EXTRA_SUBMATERI_ID, submateri.id)
                intent.putExtra(DetailSubmateriActivity.EXTRA_SUBMATERI_TITLE, submateri.title)
                intent.putExtra(DetailSubmateriActivity.EXTRA_SUBMATERI_DESCRIPTION, submateri.description)
                intent.putExtra(DetailSubmateriActivity.EXTRA_SUBMATERI_CONTENT, submateri.content)
                intent.putExtra(DetailSubmateriActivity.EXTRA_MATERI_ID, materiId)
                itemView.context.startActivity(intent)
            }
        }
    }
}
