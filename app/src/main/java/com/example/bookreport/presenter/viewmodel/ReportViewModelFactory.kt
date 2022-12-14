package com.example.bookreport.presenter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.domain.ReportUseCase
import javax.inject.Inject


class ReportViewModelFactory @Inject constructor(private val useCase: ReportUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReportViewModel(useCase) as T
    }
}

