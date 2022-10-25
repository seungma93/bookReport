package com.example.bookreport.presenter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreport.BookViewModel
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.data.entity.KakaoBookMeta
import com.example.bookreport.data.remote.KakaoRemoteDataSourceImpl
import com.example.bookreport.databinding.FragmentBookSearchBinding
import com.example.bookreport.domain.KakaoBookUseCaseImpl
import com.example.bookreport.network.BookRetrofitImpl
import com.example.bookreport.repository.KakaoBookRepositoryImpl


class BookSearchFragment: Fragment() {
    private lateinit var model: BookViewModel
    private lateinit var binding: FragmentBookSearchBinding
    private var bookData = mutableListOf<KakaoBook>()
    private lateinit var bookMetaData : KakaoBookMeta
    private var bookDataHaveKey = mutableListOf<KakaoBook>()
    private var adapter : BookListAdapter? = null
    private val onScrollListener : RecyclerView.OnScrollListener = OnScrollListener()
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BookListAdapter { KakaoBook ->
            Toast.makeText(requireContext(), "참가자 ${KakaoBook.title} 입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            val kakaoRemoteDataSourceImpl = KakaoRemoteDataSourceImpl(BookRetrofitImpl)
            val kakaoBookRepositoryImpl = KakaoBookRepositoryImpl(kakaoRemoteDataSourceImpl)
            val kakaoBookUseCaseImpl = KakaoBookUseCaseImpl(kakaoBookRepositoryImpl)
            val factory = BookViewModelFactory(kakaoBookUseCaseImpl)
            model = ViewModelProvider(this, factory).get(BookViewModel::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            if(it.meta != null) {
                bookMetaData = it.meta
            }
            bookData = it.documents as MutableList<KakaoBook>
            Log.v("구독", "구독")
                adapter?.setItems(bookData)
        }
        model.liveDataHaveKey.observe(viewLifecycleOwner){
            if(it.meta != null) {
                bookMetaData = it.meta
            }
            bookDataHaveKey = it.documents as MutableList<KakaoBook>
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

