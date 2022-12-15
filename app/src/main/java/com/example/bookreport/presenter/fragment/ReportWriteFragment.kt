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
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.databinding.FragmentReportWriteBinding
import com.example.bookreport.di.DaggerBookReportComponent
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named


class ReportWriteFragment : Fragment() {
    private var _binding: FragmentReportWriteBinding? = null
    private val binding get() = _binding!!
    private val kakaoBook get() = requireArguments().getSerializable(BookSearchFragment.KAKAO_BOOK_KEY) as BookAndBookMark

    @Inject
    @Named("ReportViewModelFactory")
    lateinit var reportViewModelFactory: ViewModelProvider.Factory
    private val reportViewModel: ReportViewModel by viewModels { reportViewModelFactory }
    @Inject
    @Named("BookMarkViewModelFactory")
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }
    @Inject
    @Named("BookListViewModelFactory")
    lateinit var bookListViewModelFactory: ViewModelProvider.Factory
    private val bookListViewModel: BookViewModel by activityViewModels { bookListViewModelFactory }

    override fun onAttach(context: Context) {
        DaggerBookReportComponent.factory().create(context).inject(this)
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
                    reportViewModel.save(report)
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
/*
    fun subscribe() {
        bookMarkViewModel.bookMarkLiveData.observe(viewLifecycleOwner) {
            binding.btnBookmark.isSelected = false
            it.bookMarks.map {
                binding.btnBookmark.isSelected = it.title == kakaoBook.book.title
            }
        }
    }
 */

    private fun subscribe() = lifecycleScope.launchWhenStarted {
        bookMarkViewModel.bookMarkState.filterNotNull().collectLatest {
            binding.btnBookmark.isSelected = false
            it.bookMarks.map {
                binding.btnBookmark.isSelected = it.title == kakaoBook.book.title
            }
        }
    }
}