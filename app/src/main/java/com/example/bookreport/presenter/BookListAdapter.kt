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

sealed class Item {
    data class BookData(val bookData: BookAndBookMark?) : Item()
    object LoadingState : Item()
}

class BookListAdapter(
    private val itemClick: (BookAndBookMark) -> Unit,
    private val bookMarkClick: (BookAndBookMark, Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }

    val datalist = mutableListOf<Item>()

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                val binding =
                    BookListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder(binding, itemClick, bookMarkClick)
            }
            else -> {
                Log.v("BookListAdapter", " onCreateViewHolder")
                val binding =
                    ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LoadingViewHolder(binding)
            }
        }


    }

    // 뷰홀더에서 아이템 바인드
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val item = datalist[position] as? Item.BookData
            kotlin.runCatching {
                if (item?.bookData != null) holder.bind(
                    item.bookData,
                    position
                ) else throw NullPointerException("에러")
            }.onFailure {
                it.printStackTrace()
            }

        } else {
        }
    }

    // 아이템 갯수
    override fun getItemCount(): Int {
        return datalist.size
    }

    fun setItems(newItems: List<BookAndBookMark>) {
        Log.v("setItem", "")
        // data 초기화
        datalist.clear()
        // 모든 데이터 add
        val item = newItems.map { Item.BookData(it) }
        val loadingItem = Item.LoadingState
        datalist.addAll(item)
        datalist.add(loadingItem)
        // 데이터 변경을 알림
        notifyDataSetChanged()
    }


    fun unsetLoading() {
        val item = Item.LoadingState
        datalist.remove(item)
        notifyItemRemoved(itemCount - 1)
    }

    class LoadingViewHolder(private val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun getItemViewType(position: Int): Int {
        return when (datalist[position]) {
            is Item.LoadingState -> TYPE_LOADING
            is Item.BookData -> TYPE_ITEM
        }
    }

    // 뷰홀더 클래스
    class ViewHolder(
        private val binding: BookListItemBinding,
        private val itemClick: (BookAndBookMark) -> Unit,
        private val bookMarkClick: (BookAndBookMark, Boolean) -> Unit
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
                    when(binding.btnBookmark.isSelected){
                        true -> bookMarkClick(it, true)
                        false -> bookMarkClick(it, false)
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

