package com.example.bookreport

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.bookreport.databinding.FragmentBookSearchBinding
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Runnable
class BookSearchFragment: Fragment() {
    private val model: BookViewModel by activityViewModels()
    private lateinit var binding: FragmentBookSearchBinding
    private var bookData = mutableListOf<Book>()
    private lateinit var bookMetaData : BookMeta
    private var bookDataHaveKey = mutableListOf<Book>()
    private var adapter : BookListAdapter? = null
    private var isLoading = false
    private val onScrollListener : RecyclerView.OnScrollListener = OnScrollListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BookListAdapter { Book ->
            Toast.makeText(requireContext(), "참가자 ${Book.title} 입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookSearchBinding.inflate(inflater,container,false)
        subscribe()
        initScrollListener()
        binding.apply {
            bookList.adapter = adapter
            btnSearch.setOnClickListener{
                val keyword = edit.text.toString()
                if( keyword.isNotEmpty()){
                    model.insertKey(keyword, 1)
                }
            }
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        //adapter.resetItem()
        Log.v("pause", "pause")
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.bookList.removeOnScrollListener(onScrollListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
    // 뷰 모델 구독
    private fun subscribe() {
        // liveData 옵저버 viewLifecycleOwner : 라이프사이클 상태를 다양하게 가지고 있음
        model.liveData.observe(viewLifecycleOwner) {
            // 변경된 liveData 삽입
            bookMetaData = it.meta
            bookData = it.documents as MutableList<Book>
            Log.v("구독", "구독")
                adapter?.setItems(bookData)
        }
        model.liveDataHaveKey.observe(viewLifecycleOwner){
            bookMetaData = it.meta
            bookDataHaveKey = it.documents as MutableList<Book>
            adapter?.addItems(bookData)
        }
    }
    // 스크롤 리스너 -> 뷰의 마지막에 닿았을때 동작
    private fun initScrollListener(){
        binding.bookList.addOnScrollListener(onScrollListener)
    }

    private fun moreItems(){
        //Log.v("키워드", "$keyword")
        val keyword = binding.edit.text.toString()
        val page = ((adapter?.itemCount)?.div(10) ?: 10) + 1
        Log.v("페이지", page.toString())
        model.insertPage(keyword, page)
        isLoading = false
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            Log.v("initscroll","스크롤")
            if(!isLoading){
                if ((recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() == adapter?.itemCount?.minus(
                        1
                    ) ?: 10){
                    Log.v("initscroll", "스크롤2")
                    Log.v("isend", "${bookMetaData.isEnd}")
                    // 다음 페이지가 있을때 동작
                    if(bookMetaData.isEnd == false) {
                        isLoading = true
                        Log.v("initscroll", "스크롤3")
                        moreItems()
                    }
                }
            }
        }

    }

}

