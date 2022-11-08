package com.example.bookreport.presenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentReportListBinding
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.repository.ReportRepositoryImpl

class ReportListFragment : Fragment() {
    private lateinit var binding: FragmentReportListBinding
    private val viewModel: ReportViewModel by lazy {
        val reportLocalDataSourceImpl = ReportLocalDataSourceImpl(requireContext())
        val reportRepositoryImpl = ReportRepositoryImpl(reportLocalDataSourceImpl)
        val saveReportUseCaseImpl = ReportUseCaseImpl(reportRepositoryImpl)
        val factory = ReportViewModelFactory(saveReportUseCaseImpl)
        ViewModelProvider(this, factory).get(ReportViewModel::class.java)
    }
    private var adapter: ReportListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = ReportListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReportListBinding.inflate(inflater, container, false)

        viewModel.load()
        subscribe()

        binding.apply {
            reportListView.adapter = adapter
            btnFloating.setOnClickListener {
                val endPoint = EndPoint.Search(0)
                (requireActivity() as? BookReport)?.navigateFragment(endPoint)
                btnFloating.hide()
            }
            return binding.root
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

    // 뷰 모델 구독
    private fun subscribe() {
        // liveData 옵저버 viewLifecycleOwner : 라이프사이클 상태를 다양하게 가지고 있음
        viewModel.liveData.observe(viewLifecycleOwner) {
            // 변경된 liveData 삽입
            adapter?.setItems(it.documents as MutableList<Report>)
        }
    }
}