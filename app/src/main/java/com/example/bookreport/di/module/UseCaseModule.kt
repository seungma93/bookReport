package com.example.bookreport.di.module

import com.example.bookreport.domain.*
import com.example.bookreport.repository.BookMarkRepository
import com.example.bookreport.repository.BookRepository
import com.example.bookreport.repository.ReportRepository
import dagger.Module
import dagger.Provides

class UseCaseModule {

    @Module
    class BookUseCaseModule {
        @Provides
        fun providesBookUseCase(
            BookRepository: BookRepository,
            bookMarkRepository: BookMarkRepository
        ): BookUseCase {
            return BookUseCaseImpl(BookRepository, bookMarkRepository)
        }
    }

    @Module
    class ReportUseCaseModule {
        @Provides
        fun providesReportUseCase(repository: ReportRepository): ReportUseCase {
            return ReportUseCaseImpl(repository)
        }
    }

    @Module
    class BookMarkUseCaseModule {
        @Provides
        fun providesBookMarkUseCase(repository: BookMarkRepository): BookMarkUseCase {
            return BookMarkUseCaseImpl(repository)
        }
    }

}
