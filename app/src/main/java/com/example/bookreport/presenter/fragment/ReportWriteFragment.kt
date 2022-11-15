package com.example.bookreport.presenter.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.BookAndBookMark
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.example.bookreport.databinding.FragmentReportWriteBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.KakaoBookUseCaseImpl
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.network.BookRetrofitImpl
import com.example.bookreport.presenter.*
import com.example.bookreport.presenter.viewmodel.*
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.KakaoBookRepositoryImpl
import com.example.bookreport.repository.ReportRepositoryImpl


class ReportWriteFragment : Fragment() {
    private lateinit var binding: FragmentReportWriteBinding
    private val kakaoBook get() = requireArguments().getSerializable(BookSearchFragment.KAKAO_BOOK_KEY) as BookAndBookMark
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
        binding = FragmentReportWriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribe()
        binding.apply {
            bookTitle.text = kakaoBook.book.title
            bookContents.text = kakaoBook.book.contents
            Glide.with(requireContext()).load(kakaoBook.book.thumbnail).into(bookThumbnail)

            btnReportSubmit.setOnClickListener {
                val report = Report(kakaoBook.book.title, kakaoBook.book.thumbnail!!, reportContext.text.toString())
                viewModel.save(report)
                (requireActivity() as? BookReport)?.navigateFragment(EndPoint.ReportList(0))
            }

            btnBookmark.setOnClickListener{
                if(!(btnBookmark.isSelected)){
                    bookMarkViewModel.saveBookMark(bookMark = BookMark(kakaoBook.book.title))
                    bookListViewModel.refreshKey()
                    btnBookmark.isSelected = true
                }else{
                    bookMarkViewModel.deleteBookMark(bookMark = BookMark(kakaoBook.book.title))
                    bookListViewModel.refreshKey()
                    btnBookmark.isSelected = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.v("ReportWriteFrgment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v("ReportWriteFrgment", "onDestroy")
    }

    fun subscribe() {
        bookMarkViewModel.bookMarkLiveData.observe(viewLifecycleOwner){
            it.bookMarks.map{
                binding.btnBookmark.isSelected = it.title == kakaoBook.book.title
            }

        }
    }

}