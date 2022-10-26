package com.example.bookreport.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.BookViewModel
import com.example.bookreport.domain.KakaoBookUseCase
import com.example.bookreport.domain.KakaoBookUseCaseImpl

class BookViewModelFactory(private val useCase: KakaoBookUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T{

        return BookViewModel(useCase) as T

    }
}

