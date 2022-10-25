package com.example.bookreport.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.databinding.BookListItemBinding

class BookListAdapter(private val itemClick: (KakaoBook) -> Unit): RecyclerView.Adapter<BookListAdapter.ViewHolder>() {
    private val datalist = mutableListOf<KakaoBook>()//리사이클러뷰에서 사용할 데이터 미리 정의 -> 나중에 MainActivity등에서 datalist에 실제 데이터 추가
    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BookListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClick)
    }
    // 뷰홀더에서 아이템 바인드
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position], position)
    }
    // 아이템 갯수
    override fun getItemCount(): Int {
        return datalist.size
    }
    // 아이템 변경시 호출
    fun setItems(newItems: List<KakaoBook>) {
        // data 초기화
        datalist.clear()
        // 모든 데이터 add
        datalist.addAll(newItems)
        // 데이터 변경을 알림
        notifyDataSetChanged()
    }
    fun addItems(newItems: List<KakaoBook>) {
        Log.v("addItems","애드아이템" )
        // 데이터 add
        datalist.addAll(newItems)
        // 데이터 변경을 알림
        notifyDataSetChanged()
    }
    fun resetItem(){
        datalist.clear()
        notifyDataSetChanged()
    }
    // 뷰홀더 클래스
    class ViewHolder(private val binding: BookListItemBinding, private val itemClick: (KakaoBook) -> Unit): RecyclerView.ViewHolder(binding.root){
        private var book: KakaoBook? = null

        init {
            binding.root.setOnClickListener {
                book?.let {
                    itemClick(it)
                }
            }
        }
        // 아이템 바인드 펑션
        fun bind(book: KakaoBook, position: Int) {
            this.book = book
            binding.apply {
                bookNo.text = position.toString()
                bookTitle.text = book.title
                bookContents.text = book.contents
                Glide.with(itemView.context).load(book.thumbnail).into(bookThumbnail)
                Log.v("아이템", position.toString())
            }
        }
    }
}

