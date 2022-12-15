package com.example.bookreport.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.example.bookreport.domain.*
import com.example.bookreport.network.BookRetrofitImpl
import com.example.bookreport.presenter.fragment.*
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.presenter.viewmodel.BookViewModelFactory
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.*
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Component(
    modules = [
        KakaoBookDataSourceModule::class,
        KakaoBookRepositoryModule::class,
        KakaoBookUseCaseModule::class,
        BookListViewModelFactoryModule::class,
        ReportDataSourceModule::class,
        ReportRepositoryModule::class,
        ReportUseCaseModule::class,
        ReportViewModelFactoryModule::class,
        BookMarkDataSourceModule::class,
        BookMarkRepositoryModule::class,
        BookMarkUseCaseModule::class,
        BookMarkViewModelFactoryModule::class
    ]
)
interface BookReportComponent {
    fun inject(fragment: BookSearchFragment)
    fun inject(fragment: ReportEditFragment)
    fun inject(fragment: ReportWriteFragment)
    fun inject(fragment: BookMarkListFragment)
    fun inject(fragment: ReportListFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BookReportComponent
    }
}

@Module
class KakaoBookDataSourceModule {
    @Provides
    fun providesKakaoRemoteDataSource(): KakaoRemoteDataSource {
        return BookRetrofitImpl.getRetrofit().create(KakaoRemoteDataSource::class.java)
    }
}

@Module
class KakaoBookRepositoryModule {
    @Provides
    fun providesKakaoBookRepository(dataSource: KakaoRemoteDataSource): KakaoBookRepository {
        return KakaoBookRepositoryImpl(dataSource)
    }
}

@Module
class KakaoBookUseCaseModule {
    @Provides
    fun providesKakaoBookUseCase(
        kakaoBookRepository: KakaoBookRepository,
        bookMarkRepository: BookMarkRepository
    ): KakaoBookUseCase {
        return KakaoBookUseCaseImpl(kakaoBookRepository, bookMarkRepository)
    }
}

@Module
class BookListViewModelFactoryModule {
    @Provides
    @Named ("BookListViewModelFactory")
    fun providesBookListViewModelFactory(useCase: KakaoBookUseCase): ViewModelProvider.Factory {
        return BookViewModelFactory(useCase)
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
        fun providesReportRepository(localDataSource: ReportLocalDataSource): ReportRepository {
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
    class ReportViewModelFactoryModule {
        @Provides
        @Named ("ReportViewModelFactory")
        fun providesReportViewModel(useCase: ReportUseCase): ViewModelProvider.Factory {
            return ReportViewModelFactory(useCase)
        }
    }

    @Module
    class BookMarkDataSourceModule {
        @Provides
        fun providesBookMarkLocalDataSource(context: Context): BookMarkLocalDataSource {
            return BookMarkLocalDataSourceImpl(context)
        }
    }

    @Module
    class BookMarkRepositoryModule {
        @Provides
        fun proviedsBookMarkRepository(localDataSource: BookMarkLocalDataSource): BookMarkRepository {
            return BookMarkRepositoryImpl(localDataSource)
        }
    }

    @Module
    class BookMarkUseCaseModule {
        @Provides
        fun providesBookMarkUseCase(repository: BookMarkRepository): BookMarkUseCase {
            return BookMarkUseCaseImpl(repository)
        }
    }

    @Module
    class BookMarkViewModelFactoryModule {
        @Provides
        @Named ("BookMarkViewModelFactory")
        fun providesBookMarkViewModelFactory(useCase: BookMarkUseCase): ViewModelProvider.Factory {
            return BookMarkViewModelFactory(useCase)
        }
    }

