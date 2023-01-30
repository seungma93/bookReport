package com.example.bookreport.di.component


import android.content.Context
import com.example.bookreport.di.module.*
import com.example.bookreport.presenter.fragment.ReportEditFragment
import com.example.bookreport.presenter.fragment.ReportWriteFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DatabaseModule.BookMarkDatabaseModule::class,
        DatabaseModule.ReportDatabaseModule::class,
        DataSourceModule.BookMarkDataSourceModule::class,
        DataSourceModule.ReportDataSourceModule::class,
        RepositoryModule.BookMarkRepositoryModule::class,
        RepositoryModule.ReportRepositoryModule::class,
        UseCaseModule.BookMarkUseCaseModule::class,
        UseCaseModule.ReportUseCaseModule::class,
        ViewModelModule.BookMarkViewModelModule::class,
        ViewModelModule.ReportViewModelModule::class,
        ViewModelModule.ViewModelFactoryModule::class
    ]
)

interface ReportEditWriteFragmentComponent {
    fun inject(fragment: ReportEditFragment)
    fun inject(fragment: ReportWriteFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ReportEditWriteFragmentComponent
    }
}

