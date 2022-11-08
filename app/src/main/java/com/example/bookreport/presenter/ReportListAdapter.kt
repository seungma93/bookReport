package com.example.bookreport.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.databinding.ReportListItemBinding

class ReportListAdapter : RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {
    private val datalist = mutableListOf<Report>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ReportListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position], position)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    fun setItems(newItems: List<Report>) {
        // data 초기화
        datalist.clear()
        // 모든 데이터 add
        datalist.addAll(newItems)
        // 데이터 변경을 알림
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ReportListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(report: Report, position: Int) {
            binding.apply {
                reportNo.text = report.no.toString()
                bookTitle.text = report.title
                Glide.with(itemView.context).load(report.thumbnail).into(bookThumbnail)
                reportContext.text = report.context
            }
        }
    }
}