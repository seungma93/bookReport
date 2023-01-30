package com.example.bookreport.di.module

import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import com.example.bookreport.presenter.fragment.ReportListFragment.Companion.KAKAO_KEY
import com.example.bookreport.repository.*
import dagger.Module
import dagger.Provides

class RepositoryModule {

    @Module
    class BookDataSourceToRepositoryModule {
        @Provides
        fun providesBookDataSourceToRepository(
            kakaoDataSource: KakaoBookRemoteDataSource,
            googleDataSource: GoogleBooksRemoteDataSource,
            bookType: String
        ): BookDataSourceWrapper {
            return when (bookType) {
                KAKAO_KEY -> KakaoBookDataSourceWrapperImpl(kakaoDataSource)
                else -> GoogleBooksDataSourceWrapperImpl(googleDataSource)
            }
        }
    }

    @Module
    class BookRepositoryModule {
        @Provides
        fun providesBookRepository(dataSourceToRepository: BookDataSourceWrapper): BookRepository {
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
