package com.example.bookreport.presenter.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.example.bookreport.databinding.FragmentReportWriteBinding
import com.example.bookreport.di.DaggerBookListComponent
import com.example.bookreport.di.DaggerBookMarkComponent
import com.example.bookreport.di.DaggerReportComponent
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.KakaoBookUseCaseImpl
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.network.BookRetrofitImpl
import com.example.bookreport.presenter.*
import com.example.bookreport.presenter.viewmodel.*
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.KakaoBookRepositoryImpl
import com.example.bookreport.repository.ReportRepositoryImpl
import kotlinx.coroutines.*
import javax.inject.Inject


class ReportWriteFragment : Fragment() {
    private var _binding: FragmentReportWriteBinding? = null
    private val binding get() = _binding!!
    private val kakaoBook get() = requireArguments().getSerializable(BookSearchFragment.KAKAO_BOOK_KEY) as BookAndBookMark

    @Inject
    lateinit var reportViewModelFactory: ViewModelProvider.Factory
    private val viewModel: ReportViewModel by viewModels { reportViewModelFactory }
    @Inject
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }
    @Inject
    lateinit var bookListViewModelFactory: ViewModelProvider.Factory
    private val bookListViewModel: BookViewModel by activityViewModels { bookListViewModelFactory }

    override fun onAttach(context: Context) {
        DaggerReportComponent.factory().create(context).inject(this)
        DaggerBookMarkComponent.factory().create(context).inject(this)
        DaggerBookListComponent.factory().create(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            bookMarkViewModel.loadBookMark()
            withContext(Dispatchers.Main) {
                subscribe()
            }
        }
        binding.apply {
            bookTitle.text = kakaoBook.book.title
            bookContents.text = kakaoBook.book.contents
            Glide.with(requireContext()).load(kakaoBook.book.thumbnail).into(bookThumbnail)

            btnReportSubmit.setOnClickListener {
                val report = Report(
                    kakaoBook.book,
                    reportContext.text.toString()
                )

                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.save(report)
                    withContext(Dispatchers.Main) {
                        (requireActivity() as? BookReport)?.navigateFragment(EndPoint.ReportList(0))
                    }
                }
                true
            }

            btnBookmark.setOnClickListener {
                if (!(btnBookmark.isSelected)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        bookMarkViewModel.saveBookMark(bookMark = BookMark(kakaoBook.book.title))
                        bookListViewModel.refreshKey()
                        withContext(Dispatchers.Main) {
                            btnBookmark.isSelected = true
                        }
                    }

                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        bookMarkViewModel.deleteBookMark(bookMark = BookMark(kakaoBook.book.title))
                        bookListViewModel.refreshKey()
                        withContext(Dispatchers.IO) {
                            btnBookmark.isSelected = false
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.v("ReportWriteFrgment", "onDestroyView")
    }

    fun subscribe() {
        bookMarkViewModel.bookMarkLiveData.observe(viewLifecycleOwner) {
            binding.btnBookmark.isSelected = false
            it.bookMarks.map {
                binding.btnBookmark.isSelected = it.title == kakaoBook.book.title
            }

        }
    }

}