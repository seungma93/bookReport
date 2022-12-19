package com.example.bookreport.di

import android.content.Context
import com.example.bookreport.presenter.fragment.BookMarkListFragment
import com.example.bookreport.presenter.fragment.ReportEditFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        Module.BookMarkDatabaseModule::class,
        Module.BookMarkDataSourceModule::class,
        Module.BookMarkRepositoryModule::class,
        Module.BookMarkUseCaseModule::class,
        Module.KakaoBookDataSourceModule::class,
        Module.KakaoBookRepositoryModule::class,
        Module.KakaoBookUseCaseModule::class,
        Module.ReportDatabaseModule::class,
        Module.ReportDataSourceModule::class,
        Module.ReportRepositoryModule::class,
        Module.ReportUseCaseModule::class,
        Module.BookMarkViewModelModule::class,
        Module.BookViewModelModule::class,
        Module.ReportViewModelModule::class,
        Module.ViewModelFactoryModule::class
    ]
)

interface ReportEditComponent {
    fun inject(fragment: ReportEditFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ReportEditComponent
    }
}

