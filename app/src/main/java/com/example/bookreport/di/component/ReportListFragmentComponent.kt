package com.example.bookreport.di.component

import android.content.Context
import com.example.bookreport.di.module.*
import com.example.bookreport.presenter.fragment.ReportListFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DatabaseModule.ReportDatabaseModule::class,
        DataSourceModule.ReportDataSourceModule::class,
        RepositoryModule.ReportRepositoryModule::class,
        UseCaseModule.ReportUseCaseModule::class,
        ViewModelModule.ReportViewModelModule::class,
        ViewModelModule.ViewModelFactoryModule::class
    ]
)

interface ReportListFragmentComponent {
    fun inject(fragment: ReportListFragment)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): ReportListFragmentComponent
    }
}

