package com.example.bookreport.domain

import android.util.Log
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.repository.BookRepository

interface BookUseCase {
    suspend fun searchBook(keyword: String, startIndex: Int): BookListEntity
    suspend fun refreshBookMark(entity: BookListEntity): BookListEntity
}

class BookUseCaseImpl(
    private val bookRepository: BookRepository
) : BookUseCase {

    override suspend fun searchBook(keyword: String, startIndex: Int): BookListEntity {
        Log.v("BookUseCase", "동작")
        return bookRepository.getBookListEntity(keyword, startIndex)
    }

    override suspend fun refreshBookMark(entity: BookListEntity): BookListEntity {
        return bookRepository.refreshBookMark(entity)
    }
}

