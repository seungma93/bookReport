package com.example.bookreport.di.component

import android.content.Context
import com.example.bookreport.di.module.*
import com.example.bookreport.presenter.fragment.BookSearchFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DatabaseModule.BookMarkDatabaseModule::class,
        DataSourceModule.BookMarkDataSourceModule::class,
        RepositoryModule.BookMarkRepositoryModule::class,
        UseCaseModule.BookMarkUseCaseModule::class,
        ViewModelModule.BookMarkViewModelModule::class,
        RepositoryModule.BookDataSourceToRepositoryModule::class,
        RepositoryModule.BookRepositoryModule::class,
        UseCaseModule.BookUseCaseModule::class,
        ViewModelModule.BookListViewModelModule::class,
        ViewModelModule.ViewModelFactoryModule::class
    ],
    dependencies = [
        KakaoBookComponent::class,
        GoogleBooksComponent::class
    ]

)
interface GoogleBookSearchFragmentComponent {

    fun inject(fragment: BookSearchFragment)

    @Component.Factory
    interface Factory {
        fun create(
            kakoBookComponent: KakaoBookComponent,
            googleBooksComponent: GoogleBooksComponent,
            @BindsInstance bookType: String,
            @BindsInstance context: Context
        ): GoogleBookSearchFragmentComponent
    }
}
