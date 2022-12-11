package com.example.bookreport.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.domain.BookMarkUseCase
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.domain.ReportUseCase
import com.example.bookreport.domain.ReportUseCaseImpl
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.BookMarkRepositoryImpl
import com.example.bookreport.repository.ReportRepository
import com.example.bookreport.repository.ReportRepositoryImpl
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [
        ReportViewModelModule::class,
        ReportDataSourceModule::class,
        ReportRepositoryModule::class,
        ReportUseCaseModule::class
    ]
)
interface ReportComponent {
    fun inject(fragment: Fragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ReportComponent
    }

}

@Module
class ReportDataSourceModule {
    @Provides
    fun providesReportLocalDataSource(context: Context): ReportLocalDataSource {
        return ReportLocalDataSourceImpl(context)
    }
}

@Module
class ReportRepositoryModule {
    @Provides
    fun proviedsReportkRepository(localDataSource: ReportLocalDataSource): ReportRepository {
        return ReportRepositoryImpl(localDataSource)
    }
}

@Module
class ReportUseCaseModule {
    @Provides
    fun providesReportUseCase(repository: ReportRepository): ReportUseCase {
        return ReportUseCaseImpl(repository)
    }
}

@Module
class ReportViewModelModule {
    @Provides
    fun providesReportViewModel(usecase: ReportUseCase): ViewModelProvider.Factory {
        return ReportViewModelFactory(usecase)
    }
}