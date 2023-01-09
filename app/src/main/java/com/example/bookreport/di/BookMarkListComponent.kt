/*
package com.example.bookreport.di

import android.content.Context
import com.example.bookreport.presenter.fragment.BookMarkListFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        Module.BookMarkDatabaseModule::class,
        Module.BookMarkDataSourceModule::class,
        Module.BookMarkRepositoryModule::class,
        Module.BookMarkUseCaseModule::class,
        Module.BookMarkViewModelModule::class,
        Module.ViewModelFactoryModule::class

    ]
)
interface BookMarkListComponent {
    fun inject(fragment: BookMarkListFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BookMarkListComponent
    }
}*/
