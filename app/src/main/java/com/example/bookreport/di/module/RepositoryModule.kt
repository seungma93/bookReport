package com.example.bookreport.di.module

import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import com.example.bookreport.repository.*
import dagger.Module
import dagger.Provides

class RepositoryModule {

    @Module
    class KakaoBookDataSourceToRepositoryModule {
        @Provides
        fun providesKakaoBookDataSourceToRepository(dataSource: KakaoBookRemoteDataSource): DataSourceToRepository {
            return KakaoBookDataSourceToRepositoryImpl(dataSource)
        }
    }

    @Module
    class GoogleBooksDataSourceToRepositoryModule {
        @Provides
        fun providesGoogleBooksDataSourceToRepository(dataSource: GoogleBooksRemoteDataSource): DataSourceToRepository {
            return GoogleBooksDataSourceToRepositoryImpl(dataSource)
        }
    }

    @Module
    class BookRepositoryModule {
        @Provides
        fun providesBookRepository(dataSourceToRepository: DataSourceToRepository): BookRepository {
            return BookRepositoryImpl(dataSourceToRepository)
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
    class BookMarkRepositoryModule {
        @Provides
        fun proviedsBookMarkRepository(localDataSource: BookMarkLocalDataSource): BookMarkRepository {
            return BookMarkRepositoryImpl(localDataSource)
        }
    }


}