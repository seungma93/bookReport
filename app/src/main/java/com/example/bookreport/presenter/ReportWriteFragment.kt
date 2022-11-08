package com.example.bookreport.presenter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookreport.BookViewModel
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.data.entity.Report
import com.example.bookreport.data.entity.ReportEntity
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentReportWriteBinding
import com.example.bookreport.domain.ReportUseCase
import com.example.bookreport.domain.SaveReportUseCaseImpl
import com.example.bookreport.repository.ReportRepository
import com.example.bookreport.repository.ReportRepositoryImpl


class ReportWriteFragment : Fragment() {
    private lateinit var binding: FragmentReportWriteBinding
    private val kakaoBook get() = requireArguments().getSerializable(BookSearchFragment.KAKAO_BOOK_KEY) as KakaoBook
    private lateinit var reportLocalDataSourceImpl: ReportLocalDataSource
    private lateinit var reportRepositoryImpl: ReportRepository
    private lateinit var saveReportUseCaseImpl: ReportUseCase
    private lateinit var factory: ReportViewModelFactory
    private val viewModel: ReportViewModel by lazy {
        ViewModelProvider(this, factory).get(ReportViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportWriteBinding.inflate(inflater, container, false)
        reportLocalDataSourceImpl = ReportLocalDataSourceImpl(requireContext())
        reportRepositoryImpl = ReportRepositoryImpl(reportLocalDataSourceImpl)
        saveReportUseCaseImpl = SaveReportUseCaseImpl(reportRepositoryImpl)
        factory = ReportViewModelFactory(saveReportUseCaseImpl)

        binding.apply {
            bookTitle.text = kakaoBook.title
            bookContents.text = kakaoBook.contents
            Glide.with(requireContext()).load(kakaoBook.thumbnail).into(bookThumbnail)

            btnReportSubmit.setOnClickListener {
                val report = Report(kakaoBook.title, kakaoBook.thumbnail!!, reportContext.text.toString())
                viewModel.save(report)
                (requireActivity() as? BookReport)?.navigateFragment(EndPoint.List(0))
            }
        }
        return binding.root
    }

}