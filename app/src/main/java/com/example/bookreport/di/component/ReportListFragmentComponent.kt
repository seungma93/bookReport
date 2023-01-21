package com.example.bookreport.di.component

import com.example.bookreport.di.subcomponent.ReportViewModelSubComponent


import android.content.Context
import com.example.bookreport.presenter.fragment.ReportListFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        ReportViewModelSubComponent::class
    ]
)

interface ReportListFragmentComponent {
    fun inject(fragment: ReportListFragment)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ReportListFragmentComponent
    }
}

