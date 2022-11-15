package com.example.bookreport.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.databinding.BookmarkListItemBinding

class BookMarkListAdapter : RecyclerView.Adapter<BookMarkListAdapter.ViewHolder>() {
    private val datalist = mutableListOf<BookMark>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BookmarkListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    class ViewHolder(private val binding: BookmarkListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookMark: BookMark) {
            binding.bookTitle.text = bookMark.title

        }
    }

    fun setItems(newItems: List<BookMark>) {
        Log.v("setItem", "")
        // data 초기화
        datalist.clear()
        // 모든 데이터 add
        datalist.addAll(newItems)
        // 데이터 변경을 알림
        notifyDataSetChanged()
    }
}