package com.example.bookreport.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.BookMarkViewModel
import com.example.bookreport.domain.BookMarkUseCase


class BookMarkViewModelFactory(private val useCase: BookMarkUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookMarkViewModel(useCase) as T
    }
}

