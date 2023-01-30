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
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.room.ReportDatabase
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentReportWriteBinding
import com.example.bookreport.di.component.DaggerReportEditWriteFragmentComponent
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.ReportRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class ReportWriteFragment : Fragment() {
    private var _binding: FragmentReportWriteBinding? = null
    private val binding get() = _binding!!
    private val bookAndBookMark get() = requireArguments().getSerializable(BookSearchFragment.BOOK_AND_BOOKMARK) as BookAndBookMark


    @Inject
    lateinit var reportViewModelFactory: ViewModelProvider.Factory
    private val reportViewModel: ReportViewModel by viewModels { reportViewModelFactory }
    @Inject
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }

/*
    private val bookMarkViewModel: BookMarkViewModel by lazy {
        val bookMarkDatabase = BookMarkDatabase.getInstance(requireContext())
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(bookMarkDatabase!!)
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val bookMarkUseCaseImpl = BookMarkUseCaseImpl(bookMarkRepositoryImpl)
        val factory = BookMarkViewModelFactory(bookMarkUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(BookMarkViewModel::class.java)
    }

    private val reportViewModel: ReportViewModel by lazy {
        val reportDatabase = ReportDatabase.getInstance(requireContext())
        val reportLocalDataSourceImpl = ReportLocalDataSourceImpl(reportDatabase!!)
        val reportRepositoryImpl = ReportRepositoryImpl(reportLocalDataSourceImpl)
        val reportUseCaseImpl = ReportUseCaseImpl(reportRepositoryImpl)
        val factory = ReportViewModelFactory(reportUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(ReportViewModel::class.java)
    }
    
 */

    override fun onAttach(context: Context) {
        DaggerReportEditWriteFragmentComponent.factory().create(context).inject(this)
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
            bookTitle.text = bookAndBookMark.bookDocuments.title
            bookContents.text = bookAndBookMark.bookDocuments.contents
            Glide.with(requireContext()).load(bookAndBookMark.bookDocuments.thumbnail)
                .into(bookThumbnail)

            btnReportSubmit.setOnClickListener {
                val report = Report(
                    bookAndBookMark.bookDocuments,
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
                        bookMarkViewModel.saveBookMark(bookMark = BookMark(bookAndBookMark.bookDocuments.title))
                        //bookListViewModel.refreshKey()
                        withContext(Dispatchers.Main) {
                            btnBookmark.isSelected = true
                        }
                    }

                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        bookMarkViewModel.deleteBookMark(bookMark = BookMark(bookAndBookMark.bookDocuments.title))
                        //bookListViewModel.refreshKey()
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
                binding.btnBookmark.isSelected = it.title == bookAndBookMark.bookDocuments.title
            }
        }
    }
}