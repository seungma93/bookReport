/*
package com.example.bookreport.di

import android.content.Context
import com.example.bookreport.presenter.fragment.BookSearchFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        Module.BookMarkDatabaseModule::class,
        Module.BookMarkDataSourceModule::class,
        Module.BookMarkRepositoryModule::class,
        Module.BookMarkUseCaseModule::class,
    //    Module.KakaoBookDataSourceModule::class,
    //    Module.KakaoBookRepositoryModule::class,
    //    Module.KakaoBookUseCaseModule::class,
        Module.BookMarkViewModelModule::class,
    //    Module.BookViewModelModule::class,
        Module.ViewModelFactoryModule::class
    ]
)

interface BookSearchComponent {
    fun inject(fragment: BookSearchFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BookSearchComponent
    }
}




*/
