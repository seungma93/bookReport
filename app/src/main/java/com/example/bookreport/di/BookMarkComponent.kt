package com.example.bookreport.di

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookreport.data.local.BookMarkLocalDataSource
import com.example.bookreport.data.local.BookMarkLocalDataSourceImpl
import com.example.bookreport.domain.BookMarkUseCase
import com.example.bookreport.domain.BookMarkUseCaseImpl
import com.example.bookreport.presenter.viewmodel.BookMarkViewModelFactory
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.BookMarkRepositoryImpl
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

@Component(
    modules = [
        BookMarkViewModelModule::class,
        BookMarkDataSourceModule::class,
        BookMarkRepositoryModule::class,
        BookMarkUseCaseModule::class
    ]
)
interface BookMarkComponent {
    fun inject(fragment: Fragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BookMarkComponent
    }

}

@Module
class BookMarkViewModelModule {
    @Provides
    fun providesBookMarkViewModel(usecase: BookMarkUseCase): ViewModelProvider.Factory {
        return BookMarkViewModelFactory(usecase)
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