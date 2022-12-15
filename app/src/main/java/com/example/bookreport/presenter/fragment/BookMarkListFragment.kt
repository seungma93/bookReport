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
import com.example.bookreport.databinding.FragmentBookmarkListBinding
import com.example.bookreport.di.DaggerBookReportComponent
import com.example.bookreport.presenter.adapter.BookMarkListAdapter
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class BookMarkListFragment: Fragment() {
    private var _binding: FragmentBookmarkListBinding? = null
    private val binding get() = _binding!!
    private var adapter: BookMarkListAdapter? = null
    @Inject
    @Named ("BookMarkViewModelFactory")
    lateinit var bookMarkViewModelFactory: ViewModelProvider.Factory
    private val bookMarkViewModel: BookMarkViewModel by activityViewModels { bookMarkViewModelFactory }

    override fun onAttach(context: Context) {
        DaggerBookReportComponent.factory().create(context).inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookmarkListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BookMarkListAdapter()
        viewLifecycleOwner.lifecycleScope.launch (Dispatchers.IO){
            bookMarkViewModel.loadBookMark()
            withContext(Dispatchers.Main){
                subscribe()
            }
        }
        binding.bookmarkListView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        _binding = null
    }
/*
    private fun subscribe() {
        viewModel.bookMarkLiveData.observe(viewLifecycleOwner){
            adapter?.setItems(it.bookMarks)
        }
    }
 */
    private fun subscribe() {
        lifecycleScope.launchWhenStarted {
            bookMarkViewModel.bookMarkState.filterNotNull().collectLatest {
                adapter?.setItems(it.bookMarks)
            }
        }
    }
}