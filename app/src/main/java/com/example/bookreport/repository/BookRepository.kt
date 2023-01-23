package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


interface BookRepository {
    suspend fun getBookEntity(keyword: String, page: Int): Flow<BookEntity?>
    suspend fun getNewBookEntity(keyword: String, page: Int): Flow<BookEntity?>
    fun subscribeBookEntity(): Flow<BookEntity?>
}

class BookRepositoryImpl (
    private val dataSourceToRepository: DataSourceToRepository
) : BookRepository {

    override suspend fun getBookEntity(
        keyword: String,
        page: Int
    ): Flow<BookEntity?> {
        return dataSourceToRepository.getDataSource(keyword, page)
    }

    override suspend fun getNewBookEntity(
        keyword: String,
        page: Int
    ): Flow<BookEntity?> {
        return dataSourceToRepository.getNewDataSource(keyword, page)
    }

    override fun subscribeBookEntity(): Flow<BookEntity?> =
        dataSourceToRepository.subscribeDataSource()

}