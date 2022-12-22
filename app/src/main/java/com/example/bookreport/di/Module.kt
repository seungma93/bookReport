package com.example.bookreport.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.entity.room.ReportDatabase
import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.example.bookreport.domain.*
import com.example.bookreport.network.KakaoBookRetrofitImpl
import com.example.bookreport.presenter.viewmodel.BookMarkViewModel
import com.example.bookreport.presenter.viewmodel.BookViewModel
import com.example.bookreport.presenter.viewmodel.ReportViewModel
import com.example.bookreport.presenter.viewmodel.ViewModelFactory
import com.example.bookreport.repository.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

class Module {

    @Module
    class KakaoBookDataSourceModule {
        @Provides
        fun providesKakaoRemoteDataSource(): KakaoRemoteDataSource {
            return KakaoBookRetrofitImpl.getRetrofit().create(KakaoRemoteDataSource::class.java)
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
    class ReportDatabaseModule {
        @Provides
        fun providesReportDatabase(context: Context): ReportDatabase {
            return ReportDatabase.getInstance(context.applicationContext)!!
            //Room.databaseBuilder<ReportDatabase>(applcation, ReportDatabase::class.java,"user.db").build()
        }
    }

    @Module
    class ReportDataSourceModule {
        @Provides
        fun providesReportLocalDataSource(db: ReportDatabase): ReportLocalDataSource {
            return ReportLocalDataSourceImpl(db)
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
    class BookMarkDatabaseModule {
        @Provides
        fun providesBookMarkDatabase(context: Context): BookMarkDatabase {
            return BookMarkDatabase.getInstance(context.applicationContext)!!
        }
    }

    @Module
    class BookMarkDataSourceModule {
        @Provides
        fun providesBookMarkLocalDataSource(db: BookMarkDatabase): BookMarkLocalDataSource {
            return BookMarkLocalDataSourceImpl(db)
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
    abstract class ViewModelFactoryModule {
        @Binds
        abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
    }

    @Module
    class ReportViewModelModule {
        @Provides
        @IntoMap
        @ViewModelKey(ReportViewModel::class)
        fun providesReportViewModel(useCase: ReportUseCase): ViewModel {
            return ReportViewModel(useCase)
        }
    }

    @Module
    class BookMarkViewModelModule {
        @Provides
        @IntoMap
        @ViewModelKey(BookMarkViewModel::class)
        fun providesBookMarkViewModel(useCase: BookMarkUseCase): ViewModel {
            return BookMarkViewModel(useCase)
        }
    }

    @Module
    class BookViewModelModule {
        @Provides
        @IntoMap
        @ViewModelKey(BookViewModel::class)
        fun providesBookViewModel(useCase: KakaoBookUseCase): ViewModel {
            return BookViewModel(useCase)
        }
    }
}