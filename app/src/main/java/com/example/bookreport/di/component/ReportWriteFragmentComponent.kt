package com.example.bookreport.di.component

import com.example.bookreport.di.module.ViewModelModule


import android.content.Context
import com.example.bookreport.presenter.fragment.BookMarkListFragment
import com.example.bookreport.presenter.fragment.ReportWriteFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ViewModelModule.ViewModelFactoryModule::class,
        ViewModelModule.ReportViewModelModule::class,
        ViewModelModule.BookMarkViewModelModule::class
    ]
)

interface ReportWriteFragmentComponent {
    fun inject(fragment: ReportWriteFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ReportWriteFragmentComponent
    }
}


