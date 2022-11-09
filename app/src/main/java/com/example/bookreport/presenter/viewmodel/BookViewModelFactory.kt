package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.domain.KakaoBookUseCase

class BookViewModelFactory(private val useCase: KakaoBookUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BookViewModel(useCase) as T
    }
}
