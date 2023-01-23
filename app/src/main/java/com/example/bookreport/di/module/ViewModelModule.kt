/*
package com.example.bookreport.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.presenter.viewmodel.ViewModelKey
import com.example.bookreport.domain.BookMarkUseCase
import com.example.bookreport.domain.BookUseCase
import com.example.bookreport.domain.ReportUseCase
import com.example.bookreport.presenter.viewmodel.BookListViewModel
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModel

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

class ViewModelModule {


    @Module
    abstract class ViewModelFactoryModule {
        @Binds
        abstract fun bindViewModelFactory(viewModelFactory: ViewModelProvider.Factory ) : ViewModelProvider.Factory
    }

    @Module
    class ReportViewModelModule {
        @Provides
        @IntoMap
        @ViewModelKey(ReportViewModel::class)
        fun providesReportViewModel(useCase: ReportUseCase): ViewModel {
            return ReportViewModel(useCase)
        }
    }

    @Module
    class BookMarkViewModelModule {
        @Provides
        @IntoMap
        @ViewModelKey(BookMarkViewModel::class)
        fun providesBookMarkViewModel(useCase: BookMarkUseCase): ViewModel {
            return BookMarkViewModel(useCase)
        }
    }

    @Module
    class BookListViewModelModule {
        @Provides
        @IntoMap
        @ViewModelKey(BookListViewModel::class)
        fun providesBookListViewModel(useCase: BookUseCase): ViewModel {
            return BookListViewModel(useCase)
        }
    }

}*/
