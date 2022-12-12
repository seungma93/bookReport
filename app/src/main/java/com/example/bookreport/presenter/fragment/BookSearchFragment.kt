package com.example.bookreport.presenter.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookreport.data.entity.KakaoBookMeta
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.databinding.BookListItemBinding
import com.example.bookreport.databinding.FragmentBookSearchBinding
import com.example.bookreport.di.DaggerBookListComponent
import com.example.bookreport.di.DaggerBookMarkComponent
import com.example.bookreport.presenter.BookListAdapter
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookSearchFragment : Fragment() {
    companion object {
        const val KAKAO_BOOK_KEY = "KAKAO_BOOK_KEY"
    }

    private var _binding: FragmentBookSearchBinding? = null
    private val binding get() = _binding!!
    private var _bookListItemBinding: BookListItemBinding? = null
    private val bookListItemBinding get() = _bookListItemBinding!!
    private var adapter: BookListAdapter? = null
    private val onScrollListener: RecyclerView.OnScrollListener = OnScrollListener()
    private lateinit var bookMetaData: KakaoBookMeta
    private var keyword: String = ""

    @Inject
    lateinit var bookListViewModelFactory: ViewModelProvider.Factory
    private val viewModel: BookViewModel by activityViewModels { bookListViewModelFactory }
    @Inject
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }

    override fun onAttach(context: Context) {
        DaggerBookMarkComponent.factory().create(context).inject(this)
        DaggerBookListComponent.factory().create(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookSearchBinding.inflate(inflater, container, false)
        _bookListItemBinding = BookListItemBinding.inflate(inflater, container, false)
        Log.v("BookSearchFragment", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BookListAdapter({
            //Toast.makeText(requireContext(), "참가자 ${KakaoBook.title} 입니다.", Toast.LENGTH_SHORT).show()
            val endPoint =
                EndPoint.ReportWrite(bookAndBookMark = it)
            (requireActivity() as? BookReport)?.navigateFragment(endPoint)
        }, { bookAndBookMark ->
            var isBookMark = false
                lifecycleScope.launch {
                    bookMarkViewModel.loadBookMark2().bookMarks.map { if (it.title == bookAndBookMark.bookMark?.title) isBookMark = true }
                }
            when (isBookMark) {
                true -> {
                    lifecycleScope.launch {
                        bookMarkViewModel.deleteBookMark(bookMark = BookMark(bookAndBookMark.book.title))
                        viewModel.refreshKey()
                    }
                    false
                }
                false -> {
                    lifecycleScope.launch {
                        bookMarkViewModel.saveBookMark(bookMark = BookMark(bookAndBookMark.book.title))
                        viewModel.refreshKey()
                    }
                    true
                }
            }
        })
        subscribe()
        initScrollListener()
        binding.apply {
            bookList.adapter = adapter
            btnSearch.setOnClickListener {
                keyword = edit.text.toString()
                if (keyword.isNotEmpty()) {
                    viewModel.insertNewKey(keyword, 1)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.v("BookSearchFragment", "pause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        binding.bookList.removeOnScrollListener(onScrollListener)
        _binding = null
        _bookListItemBinding = null
        Log.v("BookSearchFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("BookSearchFragment", "onDestroy")
    }

    // 뷰 모델 구독
    private fun subscribe() {
        viewModel.bookLiveData.observe(viewLifecycleOwner) {
            // 변경된 liveData 삽입
            if (it.meta != null) {
                bookMetaData = it.meta
            }
            Log.v("subscribe", "구독")
            adapter?.setItems(it.entities)
            Log.v("아답터아이템갯수", adapter!!.itemCount.toString())
        }
    }

    // 스크롤 리스너 -> 뷰의 마지막에 닿았을때 동작
    private fun initScrollListener() {
        binding.bookList.addOnScrollListener(onScrollListener)
    }

    private fun moreItems() {
        val keyword = binding.edit.text.toString()
        val page = ((adapter?.itemCount)?.div(10) ?: 10) + 1
        Log.v("페이지", page.toString())
        viewModel.insertKey(keyword, page)
    }

    private inner class OnScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val lastVisibleItemPosition =
                (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastVisibleItemPosition()
            val itemCount = (adapter?.itemCount?.minus(1) ?: 10)

            if (itemCount == lastVisibleItemPosition) {
                if (bookMetaData.isEnd == false) {
                    moreItems()
                } else {
                    Toast.makeText(requireContext(), "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show()
                    binding.bookList.post {
                        adapter?.unsetLoading()
                    }
                }
            }
        }
    }

}

