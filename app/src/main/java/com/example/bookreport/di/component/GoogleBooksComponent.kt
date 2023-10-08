package com.example.bookreport.di.component

import android.content.Context
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.di.module.DataSourceModule
import com.example.bookreport.di.module.RepositoryModule
import com.example.bookreport.presenter.fragment.BookSearchFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DataSourceModule.GoogleBooksDataSourceModule::class
    ]
)
interface GoogleBooksComponent {

    fun getGoogleBooksComponent(): GoogleBooksRemoteDataSource

    @Component.Factory
    interface Factory {
        fun create(): GoogleBooksComponent
    }
}
