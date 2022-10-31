package com.example.bookreport.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.domain.ReportUseCase


class ReportViewModelFactory(private val useCase: ReportUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReportViewModel(useCase) as T
    }
}

