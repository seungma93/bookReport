package com.example.bookreport.di.component

import android.content.Context
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import com.example.bookreport.di.module.*
import com.example.bookreport.presenter.fragment.BookSearchFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DataSourceModule.KakaoBookDataSourceModule::class
    ]
) interface KakaoBookComponent {

    fun getKakaoBookComponent(): KakaoBookRemoteDataSource

    @Component.Factory
    interface Factory {
        fun create(): KakaoBookComponent
    }
}
