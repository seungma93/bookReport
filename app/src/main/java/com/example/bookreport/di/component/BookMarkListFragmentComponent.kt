/*
package com.example.bookreport.di.component

import android.content.Context
import com.example.bookreport.di.module.*
import com.example.bookreport.di.scope.CustomScope
import com.example.bookreport.di.subcomponent.BookMarkViewModelSubComponent
import com.example.bookreport.presenter.fragment.BookMarkListFragment
import com.example.bookreport.presenter.fragment.BookSearchFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent

@CustomScope
@Subcomponent(
    modules = [
        BookMarkViewModelSubComponent::class
    ]
) interface BookMarkListFragmentComponent {

    fun inject(fragment: BookMarkListFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BookMarkListFragmentComponent
    }
}
*/
