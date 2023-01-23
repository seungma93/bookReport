/*
package com.example.bookreport.di.module

import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.entity.room.ReportDatabase
import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import com.example.bookreport.network.GoogleBooksRetrofitImpl
import com.example.bookreport.network.KakaoBookRetrofitImpl
import dagger.Module
import dagger.Provides

class DataSourceModule {

    @Module
    class KakaoBookDataSourceModule {
        @Provides
        fun providesKakaoRemoteDataSource(): KakaoBookRemoteDataSource {
            return KakaoBookRetrofitImpl.getRetrofit().create(KakaoBookRemoteDataSource::class.java)
        }
    }

    @Module
    class GoogleBooksDataSourceModule {
        @Provides
        fun providesGoogleBooksDataSource(): GoogleBooksRemoteDataSource {
            return GoogleBooksRetrofitImpl.getRetrofit().create(GoogleBooksRemoteDataSource::class.java)
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
    class BookMarkDataSourceModule {
        @Provides
        fun providesBookMarkLocalDataSource(db: BookMarkDatabase): BookMarkLocalDataSource {
            return BookMarkLocalDataSourceImpl(db)
        }
    }

}*/
