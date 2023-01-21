package com.example.bookreport.di.subcomponent

import com.example.bookreport.di.module.*
import dagger.Subcomponent


@Subcomponent(
    modules = [
        DatabaseModule.BookMarkDatabaseModule::class,
        DataSourceModule.BookMarkDataSourceModule::class,
        RepositoryModule.BookMarkRepositoryModule::class,
        KakaoBookUseCaseSubComponent::class,
        GoogleBooksUseCaseSubComponent::class,
        ViewModelModule.BookListViewModelModule::class
    ]
)

interface BookListViewModelSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): BookListViewModelSubComponent
    }
}

