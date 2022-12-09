package com.example.bookreport.presenter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.example.bookreport.databinding.FragmentBookSearchBinding
import com.example.bookreport.databinding.FragmentReportEditBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.KakaoBookUseCaseImpl
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.network.BookRetrofitImpl
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.viewmodel.*
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.KakaoBookRepositoryImpl
import com.example.bookreport.repository.ReportRepositoryImpl
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class ReportEditFragment : Fragment() {
    companion object {
        const val REPORT_KEY = "REPORT_KEY"
    }

    private var _binding: FragmentReportEditBinding? = null
    private val binding get() = _binding!!

    private val report get() = requireArguments().getSerializable(ReportEditFragment.REPORT_KEY) as Report
    private val viewModel: ReportViewModel by lazy {
        val reportLocalDataSourceImpl = ReportLocalDataSourceImpl(requireContext())
        val reportRepositoryImpl = ReportRepositoryImpl(reportLocalDataSourceImpl)
        val ReportUseCaseImpl = ReportUseCaseImpl(reportRepositoryImpl)
        val factory = ReportViewModelFactory(ReportUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(ReportViewModel::class.java)
    }
    private val bookMarkViewModel: BookMarkViewModel by lazy {
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(requireContext())
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val bookMarkUseCaseImpl = BookMarkUseCaseImpl(bookMarkRepositoryImpl)
        val factory = BookMarkViewModelFactory(bookMarkUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(BookMarkViewModel::class.java)
    }
    private val bookListViewModel: BookViewModel by lazy {
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(requireContext())
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val kakaoRemoteDataSourceImpl =
            BookRetrofitImpl.getRetrofit().create(KakaoRemoteDataSource::class.java)
        val kakaoBookRepositoryImpl = KakaoBookRepositoryImpl(kakaoRemoteDataSourceImpl)
        val kakaoBookUseCaseImpl =
            KakaoBookUseCaseImpl(kakaoBookRepositoryImpl, bookMarkRepositoryImpl)
        val factory = BookViewModelFactory(kakaoBookUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(BookViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bookTitle.text = report.book.title
            bookContents.text = report.book.contents
            Glide.with(requireContext()).load(report.book.thumbnail).into(bookThumbnail)
            reportContext.setText(report.context)

            CoroutineScope(Dispatchers.IO).launch {
                bookMarkViewModel.loadBookMark()
                withContext(Dispatchers.Main) {
                    subscribe()
                }
            }

            btnReportSubmit.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val context = binding.reportContext.text.toString()
                    val report = Report(book = report.book, context = context, no = report.no)
                    viewModel.edit(report)
                    viewModel.load()
                    withContext(Dispatchers.Main) {
                        val endPoint = EndPoint.ReportList(1)
                        (requireActivity() as? BookReport)?.navigateFragment(endPoint)
                    }
                }
            }
            btnBookmark.setOnClickListener {
                if (btnBookmark.isSelected.not()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        bookMarkViewModel.saveBookMark(bookMark = BookMark(report.book.title))
                        bookListViewModel.refreshKey()
                        withContext(Dispatchers.Main) {
                            btnBookmark.isSelected = true
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        bookMarkViewModel.deleteBookMark(bookMark = BookMark(report.book.title))
                        bookListViewModel.refreshKey()
                        withContext(Dispatchers.Main) {
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
    }

    fun subscribe() {
        bookMarkViewModel.bookMarkLiveData.observe(viewLifecycleOwner) {
            binding.btnBookmark.isSelected = false
            it.bookMarks.map {
                if (it.title == report.book.title) {
                    binding.btnBookmark.isSelected = true
                }
            }
        }
    }
}