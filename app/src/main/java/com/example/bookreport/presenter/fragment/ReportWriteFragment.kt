package com.example.bookreport.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentReportWriteBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.presenter.*
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.ReportRepositoryImpl


class ReportWriteFragment : Fragment() {
    private lateinit var binding: FragmentReportWriteBinding
    private val kakaoBook get() = requireArguments().getSerializable(BookSearchFragment.KAKAO_BOOK_KEY) as BookAndBookMark
    private val bookMarkValue get() = requireArguments().getBoolean(BookSearchFragment.BOOK_MARK_KEY) as Boolean
    private val viewModel: ReportViewModel by lazy {
        val reportLocalDataSourceImpl = ReportLocalDataSourceImpl(requireContext())
        val reportRepositoryImpl = ReportRepositoryImpl(reportLocalDataSourceImpl)
        val saveReportUseCaseImpl = ReportUseCaseImpl(reportRepositoryImpl)
        val factory = ReportViewModelFactory(saveReportUseCaseImpl)
        ViewModelProvider(this, factory).get(ReportViewModel::class.java)
    }
    private val bookMarkViewModel: BookMarkViewModel by lazy {
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(requireContext())
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val bookMarkUseCaseImpl = BookMarkUseCaseImpl(bookMarkRepositoryImpl)
        val factory = BookMarkViewModelFactory(bookMarkUseCaseImpl)
        ViewModelProvider(this, factory).get(BookMarkViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportWriteBinding.inflate(inflater, container, false)


        binding.apply {
            bookTitle.text = kakaoBook.book.title
            bookContents.text = kakaoBook.book.contents
            Glide.with(requireContext()).load(kakaoBook.book.thumbnail).into(bookThumbnail)
            //btnBookmark.isSelected = kakaoBook.bookMark != null
            btnBookmark.isSelected = bookMarkValue
            btnReportSubmit.setOnClickListener {
                val report = Report(kakaoBook.book.title, kakaoBook.book.thumbnail!!, reportContext.text.toString())
                viewModel.save(report)
                (requireActivity() as? BookReport)?.navigateFragment(EndPoint.ReportList(0))
            }

            btnBookmark.setOnClickListener{
                if(!(btnBookmark.isSelected)){
                    bookMarkViewModel.saveBookMark(bookMark = BookMark(kakaoBook.book.title))
                    btnBookmark.isSelected = true
                }else{
                    bookMarkViewModel.deleteBookMark(bookMark = BookMark(kakaoBook.book.title))
                    btnBookmark.isSelected = false
                }
            }
        }
        return binding.root
    }

}