package com.example.bookreport.presenter.fragment

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.R
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentReportListBinding
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.presenter.*
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.ReportRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ReportListFragment : Fragment() {
    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportViewModel by lazy {
        val reportLocalDataSourceImpl = ReportLocalDataSourceImpl(requireContext())
        val reportRepositoryImpl = ReportRepositoryImpl(reportLocalDataSourceImpl)
        val ReportUseCaseImpl = ReportUseCaseImpl(reportRepositoryImpl)
        val factory = ReportViewModelFactory(ReportUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(ReportViewModel::class.java)
    }
    private var adapter: ReportListAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ReportListAdapter {
            val endPoint = EndPoint.ReportEdit(it)
            (requireActivity() as? BookReport)?.navigateFragment(endPoint)
        }
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.load()
            subscribe()
        }
        binding.apply {
            reportListView.adapter = adapter
            var isFabOpen = false
            btnFloating.setOnClickListener {
                isFabOpen = toggleFab(isFabOpen)
            }
            btnFabSearch.setOnClickListener {
                val endPoint = EndPoint.BookSearch(0)
                (requireActivity() as? BookReport)?.navigateFragment(endPoint)
            }
            btnFabBookmark.setOnClickListener {
                val endPoint = EndPoint.BookMarkList(0)
                (requireActivity() as? BookReport)?.navigateFragment(endPoint)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }

    // 뷰 모델 구독
    private fun subscribe() {
        // liveData 옵저버 viewLifecycleOwner : 라이프사이클 상태를 다양하게 가지고 있음
        viewModel.liveData.observe(viewLifecycleOwner) {
            // 변경된 liveData 삽입
            adapter?.setItems(it.documents as MutableList<Report>)
        }
    }

    private fun toggleFab(isFabOpen: Boolean): Boolean {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.btnFabSearch, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabBookmark, "translationY", 0f).apply { start() }
            binding.btnFloating.setImageResource(R.drawable.btn_menu)
            return false
        } else {
            ObjectAnimator.ofFloat(binding.btnFabSearch, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabBookmark, "translationY", -400f).apply { start() }
            binding.btnFloating.setImageResource(R.drawable.btn_close)
            return true
        }
    }
}