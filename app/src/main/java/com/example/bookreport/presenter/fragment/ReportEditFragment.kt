package com.example.bookreport.presenter.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.room.ReportDatabase
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentReportEditBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.ReportRepository
import com.example.bookreport.repository.ReportRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReportEditFragment : Fragment() {
    companion object {
        const val REPORT_KEY = "REPORT_KEY"
    }

    private var _binding: FragmentReportEditBinding? = null
    private val binding get() = _binding!!
    private val report get() = requireArguments().getSerializable(ReportEditFragment.REPORT_KEY) as Report
/*
    @Inject
    lateinit var reportViewModelFactory: ViewModelProvider.Factory
    private val reportViewModel: ReportViewModel by activityViewModels { reportViewModelFactory }
    @Inject
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }

    @Inject
    lateinit var bookListViewModelFactory: ViewModelProvider.Factory
    private val bookListViewModel: BookViewModel by activityViewModels { bookListViewModelFactory }
*/

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

    override fun onAttach(context: Context) {
        //DaggerReportEditComponent.factory().create(context).inject(this)
        super.onAttach(context)
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
                    reportViewModel.edit(report)
                    reportViewModel.load()
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
                        //bookListViewModel.refreshKey()
                        withContext(Dispatchers.Main) {
                            btnBookmark.isSelected = true
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        bookMarkViewModel.deleteBookMark(bookMark = BookMark(report.book.title))
                        //bookListViewModel.refreshKey()
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
/*
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
 */

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            bookMarkViewModel.bookMarkState.filterNotNull().collectLatest { bookMarkEntity ->
                binding.btnBookmark.isSelected = false
                bookMarkEntity.bookMarks.map {
                    if (it.title == report.book.title) {
                        binding.btnBookmark.isSelected = true
                    }
                }
            }
        }
    }
}