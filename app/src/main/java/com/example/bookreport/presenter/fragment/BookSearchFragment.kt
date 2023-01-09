package com.example.bookreport.presenter.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookreport.data.entity.Meta
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import com.example.bookreport.databinding.FragmentBookSearchBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.BookUseCaseImpl
import com.example.bookreport.network.GoogleBooksRetrofitImpl
import com.example.bookreport.network.KakaoBookRetrofitImpl
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.adapter.BookListAdapter
import com.example.bookreport.presenter.fragment.ReportListFragment.Companion.GOOGLE_KEY
import com.example.bookreport.presenter.fragment.ReportListFragment.Companion.KAKAO_KEY
import com.example.bookreport.presenter.viewmodel.BookListViewModel
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.presenter.viewmodel.BookViewModelFactory
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.BookRepositoryImpl
import com.example.bookreport.repository.GoogleBooksDataSourceToRepositoryImpl
import com.example.bookreport.repository.KakaoBookDataSourceToRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class BookSearchFragment : Fragment() {
    companion object {
        const val KAKAO_BOOK_KEY = "KAKAO_BOOK_KEY"
    }

    private var _binding: FragmentBookSearchBinding? = null
    private val binding get() = _binding!!
    private var adapter: BookListAdapter? = null

    //private val onScrollListener: RecyclerView.OnScrollListener = OnScrollListener()
    private var bookMetaData: Meta? = null
    private var keyword: String = ""
    private val bookType get() = requireArguments().getSerializable(ReportListFragment.BOOK_TYPE_KEY) as String

    /*
    @Inject
    lateinit var bookListViewModelFactory: ViewModelProvider.Factory
    private val viewModel: BookViewModel by activityViewModels { bookListViewModelFactory }
    @Inject
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }
*/
    private val bookMarkViewModel: BookMarkViewModel by lazy {
        val bookMarkDatabase = BookMarkDatabase.getInstance(requireContext())
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(bookMarkDatabase!!)
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val bookMarkUseCaseImpl = BookMarkUseCaseImpl(bookMarkRepositoryImpl)
        val factory = BookMarkViewModelFactory(bookMarkUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(BookMarkViewModel::class.java)
    }

    private val bookViewModel: BookListViewModel by lazy {
        val bookMarkDatabase = BookMarkDatabase.getInstance(requireContext())
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(bookMarkDatabase!!)
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        when (bookType) {
            KAKAO_KEY -> {
                val kakaoRemoteDataSourceImpl =
                    KakaoBookRetrofitImpl.getRetrofit()
                        .create(KakaoBookRemoteDataSource::class.java)
                val kakaoBookDataSourceToRepositoryImpl =
                    KakaoBookDataSourceToRepositoryImpl(kakaoRemoteDataSourceImpl)
                val kakaoBookRepositoryImpl =
                    BookRepositoryImpl(kakaoBookDataSourceToRepositoryImpl, bookMarkRepositoryImpl)
                val kakaoBookUseCaseImpl =
                    BookUseCaseImpl(kakaoBookRepositoryImpl)
                val factory = BookViewModelFactory(kakaoBookUseCaseImpl)
                ViewModelProvider(this, factory).get(BookListViewModel::class.java)
            }
            GOOGLE_KEY -> {
                val googleBooksRemoteDataSource =
                    GoogleBooksRetrofitImpl.getRetrofit()
                        .create(GoogleBooksRemoteDataSource::class.java)
                val googleBooksDataSourceToRepositoryImpl =
                    GoogleBooksDataSourceToRepositoryImpl(googleBooksRemoteDataSource)
                val googleBooksRepositoryImpl =
                    BookRepositoryImpl(
                        googleBooksDataSourceToRepositoryImpl,
                        bookMarkRepositoryImpl
                    )
                val googleBooksUseCaseImpl =
                    BookUseCaseImpl(googleBooksRepositoryImpl)
                val factory = BookViewModelFactory(googleBooksUseCaseImpl)
                ViewModelProvider(this, factory).get(BookListViewModel::class.java)
            }
            else -> throw IllegalArgumentException()
        }
    }


    override fun onAttach(context: Context) {
        //DaggerBookSearchComponent.factory().create(context).inject(this)
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
        Log.v("BookSearchFregment", "넘어온 검색 타입" + bookType)
        adapter = BookListAdapter({
            //Toast.makeText(requireContext(), "참가자 ${KakaoBook.title} 입니다.", Toast.LENGTH_SHORT).show()
            val endPoint =
                EndPoint.ReportWrite(bookAndBookMark = it)
            (requireActivity() as? BookReport)?.navigateFragment(endPoint)
        }, { bookAndBookMark, isBookMark ->
            when (isBookMark) {
                true -> {
                    lifecycleScope.launch {
                        bookMarkViewModel.deleteBookMark(bookMark = BookMark(bookAndBookMark.bookDocuments.title))
                        bookViewModel.refreshKey()
                    }
                }
                false -> {
                    lifecycleScope.launch {
                        bookMarkViewModel.saveBookMark(bookMark = BookMark(bookAndBookMark.bookDocuments.title))
                        bookViewModel.refreshKey()
                    }
                }
            }
        })
        subscribe()
        //initScrollListener()
        binding.apply {
            bookList.adapter = adapter
            btnSearch.setOnClickListener {
                keyword = edit.text.toString()
                if (keyword.isNotEmpty()) {
                    lifecycleScope.launch {
                        bookViewModel.insertNewKey(keyword, 1)
                    }
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
        //binding.bookList.removeOnScrollListener(onScrollListener)
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
            bookViewModel.bookState.filterNotNull().collectLatest {
                if (it.meta != null) {
                    bookMetaData = it.meta
                }
                Log.v("subscribe", "구독")
                adapter?.setItems(it.entities)
            }
        }
    }

    /*
        // 스크롤 리스너 -> 뷰의 마지막에 닿았을때 동작
        private fun initScrollListener() {
            binding.bookList.addOnScrollListener(onScrollListener)
        }
    */
    private fun moreItems() {
        val keyword = binding.edit.text.toString()
        val page = ((adapter?.itemCount)?.div(10) ?: 10) + 1
        Log.v("페이지", page.toString())
        lifecycleScope.launch {
            bookViewModel.insertKey(keyword, page)


        }
    }

    /*
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

     */

}

