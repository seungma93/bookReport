package com.example.bookreport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreport.databinding.BookListItemBinding
import com.example.bookreport.databinding.FragmentBookSearchBinding

class BookListAdapter(): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {

    var datalist = mutableListOf<Book>()//리사이클러뷰에서 사용할 데이터 미리 정의 -> 나중에 MainActivity등에서 datalist에 실제 데이터 추가

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BookListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        println("뷰홀드생성")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    class ViewHolder(private val binding: BookListItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(book: Book) {
            binding.bookTitle.text = book.title
            println("바인드")
        }
    }
}

