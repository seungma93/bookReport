package com.example.bookreport.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentBookmarkListBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.presenter.BookMarkListAdapter
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.repository.BookMarkRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.zip.Inflater

class BookMarkListFragment: Fragment() {
    private var _binding: FragmentBookmarkListBinding? = null
    private val binding get() = _binding!!
    private var adapter: BookMarkListAdapter? = null
    private val viewModel: BookMarkViewModel by lazy {
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(requireContext())
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val bookMarUseCaseImpl = BookMarkUseCaseImpl(bookMarkRepositoryImpl)
        val factory = BookMarkViewModelFactory(bookMarUseCaseImpl)
        ViewModelProvider(this, factory).get(BookMarkViewModel::class.java)
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
            viewModel.loadBookMark()
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

    private fun subscribe() {
        viewModel.bookMarkLiveData.observe(viewLifecycleOwner){
            adapter?.setItems(it.bookMarks)
        }
    }
}