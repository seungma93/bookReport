package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


interface BookRepository {
    suspend fun getBookEntity(keyword: String, page: Int): Flow<BookEntity?>
    fun subscribeBookEntity(): Flow<BookEntity?>
}

class BookRepositoryImpl @Inject constructor(
    private val bookDataSourceWrapper: BookDataSourceWrapper
) : BookRepository {

    override suspend fun getBookEntity(
        keyword: String,
        page: Int
    ): Flow<BookEntity?> {
        return bookDataSourceWrapper.getDataSource(keyword, page)
    }

    override fun subscribeBookEntity(): Flow<BookEntity?> =
        bookDataSourceWrapper.subscribeDataSource()

}