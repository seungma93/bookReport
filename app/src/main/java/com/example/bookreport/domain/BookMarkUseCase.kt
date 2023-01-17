package com.example.bookreport.domain

import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.repository.BookMarkRepository
import kotlinx.coroutines.flow.Flow

interface BookMarkUseCase{
    suspend fun saveBookMark(bookMark: BookMark)
    suspend fun deleteBookMark(bookMark: BookMark)
    suspend fun loadBookMark(): Flow<List<BookMark>>
}

class BookMarkUseCaseImpl (private val bookMarkRepository: BookMarkRepository): BookMarkUseCase {
    override suspend fun saveBookMark(bookMark: BookMark) {
        bookMarkRepository.insertData(bookMark)
    }

    override suspend fun deleteBookMark(bookMark: BookMark) {
        bookMarkRepository.deleteData(bookMark)
    }

    override suspend fun loadBookMark(): Flow<List<BookMark>> {
        return bookMarkRepository.selectData()
    }
}