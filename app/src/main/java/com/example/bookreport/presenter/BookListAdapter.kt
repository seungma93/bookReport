package com.example.bookreport.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.databinding.BookListItemBinding
import com.example.bookreport.databinding.ItemLoadingBinding
import kotlinx.coroutines.delay

class BookListAdapter(
    private val itemClick: (BookAndBookMark) -> Unit,
    private val bookMarkOn: (BookAndBookMark) -> Boolean,
    private val bookMarkOff: (BookAndBookMark) -> Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    private val datalist =
        mutableListOf<BookAndBookMark?>()//리사이클러뷰에서 사용할 데이터 미리 정의 -> 나중에 MainActivity등에서 datalist에 실제 데이터 추가

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val binding =
                    BookListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(binding, itemClick, bookMarkOn, bookMarkOff)
            }
            else -> {
                Log.v("BookListAdapter"," onCreateViewHolder")
                val binding =
                    ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
        }


    }

    // 뷰홀더에서 아이템 바인드
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ViewHolder) {
            holder.bind(datalist[position]!!, position)
        }else{

        }
    }

    // 아이템 갯수
    override fun getItemCount(): Int {
        return datalist.size
    }

    // 아이템 변경시 호출
    fun setItems(newItems: List<BookAndBookMark>) {
        Log.v("setItem", "")
        // data 초기화
        datalist.clear()
        // 모든 데이터 add
        datalist.addAll(newItems)
        datalist.add(null)
        // 데이터 변경을 알림
        notifyDataSetChanged()
    }

    class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return when (datalist[position]) {
            null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    // 뷰홀더 클래스
    class ViewHolder(
        private val binding: BookListItemBinding,
        private val itemClick: (BookAndBookMark) -> Unit,
        private val bookMarkOn: (BookAndBookMark) -> Boolean,
        private val bookMarkOff: (BookAndBookMark) -> Boolean
    ) : RecyclerView.ViewHolder(binding.root) {
        private var book: BookAndBookMark? = null

        init {
            binding.root.setOnClickListener {
                book?.let {
                    itemClick(it)
                }
            }
            binding.btnBookmark.setOnClickListener {
                book?.let {
                    if (!(binding.btnBookmark.isSelected)) {
                        if (bookMarkOn(it)) binding.btnBookmark.isSelected =
                            true else Log.v("BookListAdapter", "오류발생")
                    } else {
                        if (bookMarkOff(it)) binding.btnBookmark.isSelected =
                            false else Log.v("BookListAdapter", "오류발생")
                    }
                }
            }
        }

        // 아이템 바인드 펑션
        fun bind(entity: BookAndBookMark, position: Int) = with(entity) {
            this@ViewHolder.book = this
            binding.apply {
                bookNo.text = position.toString()
                bookTitle.text = book.title
                bookContents.text = book.contents
                Glide.with(itemView.context).load(book.thumbnail).into(bookThumbnail)
                btnBookmark.isSelected = bookMark != null
                Log.v("아이템", position.toString())
            }
        }
    }
}

