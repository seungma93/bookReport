package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext


interface DataSourceToRepository {
    suspend fun toRepository(keyword: String, page: Int): Flow<BookEntity>
}

class KakaoBookDataSourceToRepositoryImpl(
    private val kakaoBookRemoteDataSource: KakaoBookRemoteDataSource
) : DataSourceToRepository {

    override suspend fun toRepository(keyword: String, page: Int): Flow<BookEntity> {
        kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
            val dataSourceFlow: Flow<BookEntity> = flow {
                emit(copy(items = items.filterNot { it.isbn.isNullOrEmpty() }))
            }
            return dataSourceFlow
        }
    }
}

class GoogleBooksDataSourceToRepositoryImpl(
    private val googleBooksRemoteDataSource: GoogleBooksRemoteDataSource
) : DataSourceToRepository {

    override suspend fun toRepository(keyword: String, page: Int): Flow<BookEntity> {
        val startIndex = 0 + 10 * (page - 1)
        googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial").toEntity()
            .run {
                val dataSourceFlow: Flow<BookEntity> = flow {
                    emit(copy(items = items.filterNot { it.isbn.isNullOrEmpty() }))
                }
                return dataSourceFlow
            }
    }
}