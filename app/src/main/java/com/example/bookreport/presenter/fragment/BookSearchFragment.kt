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
import com.example.bookreport.databinding.FragmentBookSearchBinding
import com.example.bookreport.di.DaggerBookReportComponent
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.adapter.BookListAdapter
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class BookSearchFragment : Fragment() {
    companion object {
        const val KAKAO_BOOK_KEY = "KAKAO_BOOK_KEY"
    }

    private var _binding: FragmentBookSearchBinding? = null
    private val binding get() = _binding!!
    private var adapter: BookListAdapter? = null
    private val onScrollListener: RecyclerView.OnScrollListener = OnScrollListener()
    private var bookMetaData: KakaoBookMeta? = null
    private var keyword: String = ""

    @Inject
    @Named("BookListViewModelFactory")
    lateinit var bookListViewModelFactory: ViewModelProvider.Factory
    private val viewModel: BookViewModel by activityViewModels { bookListViewModelFactory }
    @Inject
    @Named ("BookMarkViewModelFactory")
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }

    override fun onAttach(context: Context) {
        DaggerBookReportComponent.factory().create(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookSearchBinding.inflate(inflater, container, false)
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
        }, { bookAndBookMark, isBookMark ->
            when (isBookMark) {
                true -> {
                    lifecycleScope.launch {
                        bookMarkViewModel.deleteBookMark(bookMark = BookMark(bookAndBookMark.book.title))
                        viewModel.refreshKey()
                    }
                }
                false -> {
                    lifecycleScope.launch {
                        bookMarkViewModel.saveBookMark(bookMark = BookMark(bookAndBookMark.book.title))
                        viewModel.refreshKey()
                    }
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
        Log.v("BookSearchFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("BookSearchFragment", "onDestroy")
    }

    /*
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
*/
    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            viewModel.bookState.filterNotNull().collectLatest {
                if (it.meta != null) {
                    bookMetaData = it.meta
                }
                Log.v("subscribe", "구독2")
                adapter?.setItems(it.entities)
            }
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
                Log.v("BookSearchFragment", "onScrolled")
                if(bookMetaData != null){
                    if (bookMetaData?.isEnd == false) {
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

}

