package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.domain.BookMarkUseCase
import com.example.bookreport.domain.BookUseCase
import com.example.bookreport.domain.ReportUseCase
import javax.inject.Inject
import javax.inject.Provider


class ViewModelFactory (
    val viewModelMap: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelMap[modelClass]?.get() as T
    }
}


class BookListViewModelFactory(private val useCase: BookUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return BookListViewModel(useCase) as T

    }
}

class BookMarkViewModelFactory(private val useCase: BookMarkUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return BookMarkViewModel(useCase) as T

    }
}

class ReportViewModelFactory(private val useCase: ReportUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ReportViewModel(useCase) as T

    }
}



