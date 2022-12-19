package com.example.bookreport.di

import android.content.Context
import com.example.bookreport.presenter.fragment.BookMarkListFragment
import com.example.bookreport.presenter.fragment.ReportListFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        Module.ReportDatabaseModule::class,
        Module.ReportDataSourceModule::class,
        Module.ReportRepositoryModule::class,
        Module.ReportUseCaseModule::class,
        Module.ReportViewModelModule::class,
        Module.ViewModelFactoryModule::class


    ]
)

interface ReportListComponent {
    fun inject(fragment: ReportListFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ReportListComponent
    }
}

