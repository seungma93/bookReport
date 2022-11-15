package com.example.bookreport.presenter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.databinding.FragmentBookmarkListBinding
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.presenter.BookMarkListAdapter
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.repository.BookMarkRepositoryImpl
import java.util.zip.Inflater

class BookMarkListFragment: Fragment() {
    private lateinit var binding: FragmentBookmarkListBinding
    private var adapter: BookMarkListAdapter? = null
    private val viewModel: BookMarkViewModel by lazy {
        val bookMarkLocalDataSourceImpl = BookMarkLocalDataSourceImpl(requireContext())
        val bookMarkRepositoryImpl = BookMarkRepositoryImpl(bookMarkLocalDataSourceImpl)
        val bookMarUseCaseImpl = BookMarkUseCaseImpl(bookMarkRepositoryImpl)
        val factory = BookMarkViewModelFactory(bookMarUseCaseImpl)
        ViewModelProvider(this, factory).get(BookMarkViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BookMarkListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookmarkListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bookmarkListView.adapter = adapter
        viewModel.loadBookMark()
        subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }
    private fun subscribe() {
        viewModel.bookMarkLiveData.observe(viewLifecycleOwner){
            adapter?.setItems(it.bookMarks)
        }
    }
}