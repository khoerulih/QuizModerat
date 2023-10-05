package com.khoerulih.quizmoderat.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.data.Materi
import com.khoerulih.quizmoderat.databinding.ItemRowMateriBinding
import com.khoerulih.quizmoderat.ui.submateri.SubmateriActivity

class ListMateriAdapter(private val listMateri: List<Materi>) :
    RecyclerView.Adapter<ListMateriAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemBinding =
            ItemRowMateriBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = listMateri.size


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val materi: Materi = listMateri[position]
        holder.bind(materi)
    }

    class ListViewHolder(private val itemBinding: ItemRowMateriBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(materi: Materi) {
            itemBinding.tvItemMateri.text = materi.title
            Glide.with(itemView.context)
                .load(materi.icon)
                .into(itemBinding.ivItemMateri)
            itemBinding.tvItemProgress.text = itemView.context.getString(R.string.materi_progress, materi.progress)
            itemBinding.pbProgress.progress = materi.progress
            if (materi.progress > 75){
                itemBinding.pbProgress.setIndicatorColor(Color.parseColor("#60BF8B"))
            } else if (materi.progress > 50) {
                itemBinding.pbProgress.setIndicatorColor(Color.parseColor("#CACD50"))
            } else {
                itemBinding.pbProgress.setIndicatorColor(Color.parseColor("#E35858"))
            }

            itemBinding.cardViewMateri.setOnClickListener {
                val intent = Intent(itemView.context, SubmateriActivity::class.java)
                intent.putExtra(SubmateriActivity.EXTRA_MATERI_ID, materi.id)
                intent.putExtra(SubmateriActivity.EXTRA_MATERI_TITLE, materi.title)
                intent.putExtra(SubmateriActivity.EXTRA_MATERI_DESCRIPTION, materi.description)
                itemView.context.startActivity(intent)
            }
        }
    }

}