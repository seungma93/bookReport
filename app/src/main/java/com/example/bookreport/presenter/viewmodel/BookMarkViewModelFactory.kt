package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.domain.BookMarkUseCase
import javax.inject.Inject


class BookMarkViewModelFactory @Inject constructor(private val useCase: BookMarkUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookMarkViewModel(useCase) as T
    }
}

