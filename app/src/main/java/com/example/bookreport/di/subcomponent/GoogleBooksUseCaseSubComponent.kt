/*
package com.example.bookreport.di.subcomponent

import com.example.bookreport.di.module.*
import dagger.Subcomponent


@Subcomponent(
    modules = [
        DataSourceModule.GoogleBooksDataSourceModule::class,
        RepositoryModule.GoogleBooksDataSourceToRepositoryModule::class,
        RepositoryModule.BookRepositoryModule::class,
        UseCaseModule.BookUseCaseModule::class
    ]
)

interface GoogleBooksUseCaseSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): GoogleBooksUseCaseSubComponent
    }
}

*/
