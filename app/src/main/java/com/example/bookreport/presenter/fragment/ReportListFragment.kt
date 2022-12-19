package com.example.bookreport.presenter.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookreport.R
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.databinding.FragmentReportListBinding
import com.example.bookreport.di.DaggerReportListComponent
import com.example.bookreport.di.Module_BookMarkDatabaseModule_ProvidesBookMarkDatabaseFactory.create
import com.example.bookreport.di.Module_ReportViewModelModule_ProvidesReportViewModelFactory.create
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.adapter.ReportListAdapter
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReportListFragment : Fragment() {
    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!
    private var adapter: ReportListAdapter? = null
    @Inject
    lateinit var reportViewModelFactory: ViewModelProvider.Factory
    private val reportViewModel: ReportViewModel by activityViewModels { reportViewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerReportListComponent.factory().create(context).inject(this)
    }

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
            reportViewModel.load()
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

    /*
    private fun subscribe() {
        // liveData 옵저버 viewLifecycleOwner : 라이프사이클 상태를 다양하게 가지고 있음
        reportViewModel.liveData.observe(viewLifecycleOwner) {
            // 변경된 liveData 삽입
            adapter?.setItems(it.documents as MutableList<Report>)
        }
    }
     */

    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            reportViewModel.reportState.filterNotNull().collectLatest {
                adapter?.setItems(it.documents as MutableList<Report>)
            }
        }
    }

    private fun toggleFab(isFabOpen: Boolean): Boolean {
        return if (isFabOpen) {
            ObjectAnimator.ofFloat(binding.btnFabSearch, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabBookmark, "translationY", 0f).apply { start() }
            binding.btnFloating.setImageResource(R.drawable.btn_menu)
            false
        } else {
            ObjectAnimator.ofFloat(binding.btnFabSearch, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabBookmark, "translationY", -400f).apply { start() }
            binding.btnFloating.setImageResource(R.drawable.btn_close)
            true
        }
    }
}