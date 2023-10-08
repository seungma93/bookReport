package com.example.bookreport.di.component

import android.content.Context
import com.example.bookreport.di.module.*
import com.example.bookreport.presenter.fragment.BookMarkListFragment
import com.example.bookreport.presenter.fragment.BookSearchFragment
import com.example.bookreport.presenter.fragment.ReportEditFragment
import com.example.bookreport.presenter.fragment.ReportWriteFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DatabaseModule.BookMarkDatabaseModule::class,
        DataSourceModule.BookMarkDataSourceModule::class,
        RepositoryModule.BookMarkRepositoryModule::class,
        UseCaseModule.BookMarkUseCaseModule::class,
        ViewModelModule.BookMarkViewModelModule::class,
        ViewModelModule.ViewModelFactoryModule::class
    ]
)

interface BookMarkListFragmentComponent {
    fun inject(fragment: BookMarkListFragment)
    fun inject(fragment: BookSearchFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): BookMarkListFragmentComponent
    }
}

