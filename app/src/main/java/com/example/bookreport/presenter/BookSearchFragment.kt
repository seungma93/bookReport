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
import com.example.bookreport.BookMarkViewModel
import com.example.bookreport.BookViewModel
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.KakaoBookMeta
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.remote.KakaoRemoteDataSource

import com.example.bookreport.databinding.FragmentBookSearchBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.KakaoBookUseCaseImpl
import com.example.bookreport.network.BookRetrofitImpl
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.KakaoBookRepositoryImpl


class BookSearchFragment : Fragment() {
    companion object {
        const val KAKAO_BOOK_KEY = "KAKAO_BOOK_KEY"
    }

    private val viewModel: BookViewModel by lazy {
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(requireContext())
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val kakaoRemoteDataSourceImpl =
            BookRetrofitImpl.getRetrofit().create(KakaoRemoteDataSource::class.java)
        val kakaoBookRepositoryImpl = KakaoBookRepositoryImpl(kakaoRemoteDataSourceImpl)
        val kakaoBookUseCaseImpl =
            KakaoBookUseCaseImpl(kakaoBookRepositoryImpl, bookMarkRepositoryImpl)
        val factory = BookViewModelFactory(kakaoBookUseCaseImpl)
        ViewModelProvider(this, factory).get(BookViewModel::class.java)
    }
    private val bookMarkViewModel: BookMarkViewModel by lazy {
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(requireContext())
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val bookMarkUseCaseImpl = BookMarkUseCaseImpl(bookMarkRepositoryImpl)
        val factory = BookMarkViewModelFactory(bookMarkUseCaseImpl)
        ViewModelProvider(this, factory).get(BookMarkViewModel::class.java)
    }

    private lateinit var binding: FragmentBookSearchBinding
    private var adapter: BookListAdapter? = null
    private val onScrollListener: RecyclerView.OnScrollListener = OnScrollListener()
    private lateinit var bookMetaData: KakaoBookMeta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BookListAdapter({
            //Toast.makeText(requireContext(), "참가자 ${KakaoBook.title} 입니다.", Toast.LENGTH_SHORT).show()
            val endPoint = EndPoint.Write(it.book)
            (requireActivity() as? BookReport)?.navigateFragment(endPoint)
        }, {
            bookMarkViewModel.saveBookMark(bookMark = BookMark(it.book.title))
            true
        }, {
            bookMarkViewModel.deleteBookMark(bookMark = BookMark(it.book.title))
            true
        }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookSearchBinding.inflate(inflater, container, false)
        //bookMarkViewModel.loadBookMark()
        subscribe()
        initScrollListener()
        binding.apply {
            bookList.adapter = adapter
            btnSearch.setOnClickListener {
                val keyword = edit.text.toString()
                if (keyword.isNotEmpty()) {
                    //bookMarkViewModel.loadBookMark()
                    viewModel.insertNewKey(keyword, 1)
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
        adapter = null
        binding.bookList.removeOnScrollListener(onScrollListener)
    }

    // 뷰 모델 구독
    private fun subscribe() {

        viewModel.bookLiveData.observe(viewLifecycleOwner) {
            // 변경된 liveData 삽입
            if (it.meta != null) {
                bookMetaData = it.meta
            }
            Log.v("구독", "구독")
            adapter?.setItems(it.entities)
            //val page = (adapter!!.itemCount / 10) + 1
            Log.v("아답터아이템갯수", adapter!!.itemCount.toString())
            //adapter!!.notifyItemRangeInserted((page - 1) * 10, 10)

        }

    }

    // 스크롤 리스너 -> 뷰의 마지막에 닿았을때 동작
    private fun initScrollListener() {
        binding.bookList.addOnScrollListener(onScrollListener)
    }

    private fun moreItems() {
        //Log.v("키워드", "$keyword")
        val keyword = binding.edit.text.toString()
        val page = ((adapter?.itemCount)?.div(10) ?: 10) + 1
        //val page = (adapter!!.itemCount/10) + 1
        Log.v("페이지", page.toString())
        viewModel.insertKey(keyword, page)
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
            val itemCount = adapter?.itemCount?.minus(1) ?: 10
            super.onScrolled(recyclerView, dx, dy)
            Log.v("initscroll", "스크롤")

            Log.v(
                "lastVisible",
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
                    .toString()
            )
            Log.v("itmeCount", itemCount.toString())
            if (lastVisibleItemPosition == itemCount) {
                Log.v("initscroll", "스크롤2")
                Log.v("isend", "${bookMetaData.isEnd}")
                // 다음 페이지가 있을때 동작
                if (bookMetaData.isEnd == false) {
                    Log.v("initscroll", "스크롤3")
                    moreItems()
                } else {
                    Toast.makeText(requireContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}

