/*

package com.example.bookreport.di.component

import android.content.Context
import com.example.bookreport.di.module.ViewModelModule
import com.example.bookreport.di.subcomponent.BookListViewModelSubComponent
import com.example.bookreport.di.subcomponent.BookMarkViewModelSubComponent
import com.example.bookreport.presenter.fragment.BookSearchFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ViewModelModule.ViewModelFactoryModule::class,
        BookMarkViewModelSubComponent::class,
        BookListViewModelSubComponent::class
    ]
)

interface BookSearchFragmentComponent {
    fun inject(fragment: BookSearchFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BookSearchFragmentComponent
    }
}





*/
