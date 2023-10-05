package com.khoerulih.quizmoderat.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.khoerulih.quizmoderat.R
import com.khoerulih.quizmoderat.data.QuizHistory
import com.khoerulih.quizmoderat.databinding.ItemRowHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class ListQuizHistoryAdapter(private val listQuizHistory: List<QuizHistory>) :
    RecyclerView.Adapter<ListQuizHistoryAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val itemBinding =
            ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val quizHistory: QuizHistory = listQuizHistory[position]
        holder.bind(quizHistory)
    }

    override fun getItemCount(): Int = listQuizHistory.size

    class ListViewHolder(private val itemBinding: ItemRowHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(quizHistory: QuizHistory){
            val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
            val date = Date(quizHistory.timestamp)
            val datetime = sdf.format(date)

            itemBinding.tvNomorMateri.text = quizHistory.materiTitle
            itemBinding.tvSkor.text = quizHistory.score.toString()
            itemBinding.tvDatetime.text = datetime
            when(quizHistory.score){
                in 80..100 -> {
                    itemBinding.ivSkorBg.setImageResource(R.drawable.ic_skor_sangat_baik)
                    itemBinding.tvKeterangan.text = "Sangat Baik"
                    itemBinding.tvKeterangan.setTextColor(Color.parseColor("#2A9159"))
                }
                in 60..79 -> {
                    itemBinding.ivSkorBg.setImageResource(R.drawable.ic_skor_baik)
                    itemBinding.tvKeterangan.text = "Baik"
                    itemBinding.tvKeterangan.setTextColor(Color.parseColor("#60BF8B"))
                }
                in 40..59 -> {
                    itemBinding.ivSkorBg.setImageResource(R.drawable.ic_skor_cukup)
                    itemBinding.tvKeterangan.text = "Cukup"
                    itemBinding.tvKeterangan.setTextColor(Color.parseColor("#CACD50"))
                }
                in 20..49 -> {
                    itemBinding.ivSkorBg.setImageResource(R.drawable.ic_skor_kurang)
                    itemBinding.tvKeterangan.text = "Kurang"
                    itemBinding.tvKeterangan.setTextColor(Color.parseColor("#E35858"))
                }
                else -> {
                    itemBinding.ivSkorBg.setImageResource(R.drawable.ic_skor_sangat_kurang)
                    itemBinding.tvKeterangan.text = "Sangat Kurang"
                    itemBinding.tvKeterangan.setTextColor(Color.parseColor("#C52121"))
                }
            }
        }
    }
}