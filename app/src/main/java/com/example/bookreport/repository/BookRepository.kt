package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


interface BookRepository {
    suspend fun getBookListEntity(keyword: String, page: Int): MutableStateFlow<BookEntity?>
}

class BookRepositoryImpl @Inject constructor(
    private val dataSourceToRepository: DataSourceToRepository
) : BookRepository {
    override suspend fun getBookListEntity(
        keyword: String,
        page: Int
    ): MutableStateFlow<BookEntity?> {
        return dataSourceToRepository.toRepository(keyword, page)
    }
}