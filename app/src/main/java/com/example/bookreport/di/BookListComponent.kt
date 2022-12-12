package com.example.bookreport.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.data.local.ReportLocalDataSource
import com.example.bookreport.data.local.ReportLocalDataSourceImpl
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.example.bookreport.domain.*
import com.example.bookreport.network.BookRetrofitImpl
import com.example.bookreport.presenter.fragment.BookSearchFragment
import com.example.bookreport.presenter.fragment.ReportEditFragment
import com.example.bookreport.presenter.fragment.ReportWriteFragment
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.presenter.viewmodel.BookViewModelFactory
import com.example.bookreport.presenter.viewmodel.ReportViewModelFactory
import com.example.bookreport.repository.*
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [
        BookMarkDataSourceModule::class,
        BookMarkRepositoryModule::class,
        KakaoBookDataSourceModule::class,
        KakaoBookRepositoryModule::class,
        KakaoBookUseCaseModule::class,
        BookListViewModelFactoryModule::class
    ]
)
interface BookListComponent {
    fun inject(fragment: BookSearchFragment)
    fun inject(fragment: ReportEditFragment)
    fun inject(fragment: ReportWriteFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BookListComponent
    }
}

@Module
class BookListViewModelFactoryModule {
    @Provides
    fun providesBookListViewModelFactory(useCase: KakaoBookUseCase): ViewModelProvider.Factory{
        return BookViewModelFactory(useCase)
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


