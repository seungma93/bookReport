package com.example.bookreport.di.subcomponent

import com.example.bookreport.di.module.*
import com.example.bookreport.di.subcomponent.BookMarkViewModelSubComponent
import dagger.Subcomponent


@Subcomponent(
    modules = [
        DatabaseModule.ReportDatabaseModule::class,
        DataSourceModule.ReportDataSourceModule::class,
        RepositoryModule.ReportRepositoryModule::class,
        UseCaseModule.ReportUseCaseModule::class,
        ViewModelModule.ReportViewModelModule::class
    ]
)

interface ReportViewModelSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): BookMarkViewModelSubComponent
    }
}

