/*
package com.example.bookreport.di.subcomponent

import com.example.bookreport.di.module.DataSourceModule
import com.example.bookreport.di.module.RepositoryModule
import com.example.bookreport.di.module.UseCaseModule
import dagger.Subcomponent


@Subcomponent(
    modules = [
        DataSourceModule.KakaoBookDataSourceModule::class,
        RepositoryModule.KakaoBookDataSourceToRepositoryModule::class,
        RepositoryModule.BookRepositoryModule::class,
        UseCaseModule.BookUseCaseModule::class
    ]
)

interface KakaoBookUseCaseSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): KakaoBookUseCaseSubComponent
    }
}

*/
