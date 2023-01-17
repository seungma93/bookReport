package com.example.bookreport.presenter.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookreport.R
import com.example.bookreport.data.entity.room.Report
import com.example.bookreport.data.entity.room.ReportDatabase
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentReportListBinding
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.presenter.BookReport
import com.example.bookreport.presenter.EndPoint
import com.example.bookreport.presenter.adapter.ReportListAdapter
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.ReportRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ReportListFragment : Fragment() {
    companion object {
        const val BOOK_TYPE_KEY = "BOOK_TYPE_KEY"
        const val GOOGLE_KEY = "GOOGLE_KEY"
        const val KAKAO_KEY = "KAKAO_KEY"
    }
    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!
    private var adapter: ReportListAdapter? = null
    /*
    @Inject
    lateinit var reportViewModelFactory: ViewModelProvider.Factory
    private val reportViewModel: ReportViewModel by activityViewModels { reportViewModelFactory }
*/
    private val reportViewModel: ReportViewModel by lazy {
        val reportDatabase = ReportDatabase.getInstance(requireContext())
        val reportLocalDataSourceImpl = ReportLocalDataSourceImpl(reportDatabase!!)
        val reportRepositoryImpl = ReportRepositoryImpl(reportLocalDataSourceImpl)
        val reportUseCaseImpl = ReportUseCaseImpl(reportRepositoryImpl)
        val factory = ReportViewModelFactory(reportUseCaseImpl)
        ViewModelProvider(requireActivity(), factory).get(ReportViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //DaggerReportListComponent.factory().create(context).inject(this)
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
            btnFabSearchKakao.setOnClickListener {
                val endPoint = EndPoint.BookSearch(KAKAO_KEY)

                (requireActivity() as? BookReport)?.navigateFragment(endPoint)
            }
            btnFabSearchGoogle.setOnClickListener {
                val endPoint = EndPoint.BookSearch(GOOGLE_KEY)
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
            ObjectAnimator.ofFloat(binding.btnFabSearchKakao, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabSearchGoogle, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabBookmark, "translationY", 0f).apply { start() }
            binding.btnFloating.setIconResource(R.drawable.btn_menu)
            binding.btnFloating.text = "menu"
            false
        } else {
            ObjectAnimator.ofFloat(binding.btnFabSearchKakao, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabSearchGoogle, "translationY", -400f).apply { start() }
            ObjectAnimator.ofFloat(binding.btnFabBookmark, "translationY", -600f).apply { start() }
            binding.btnFloating.setIconResource(R.drawable.btn_close)
            binding.btnFloating.text = "close"
            true
        }
    }
}