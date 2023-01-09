package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface DataSourceToRepository {
    suspend fun toRepository(keyword: String, startIndex: Int): BookEntity
}

class KakaoBookDataSourceToRepositoryImpl (
    private val kakaoBookRemoteDataSource: KakaoBookRemoteDataSource
        ) : DataSourceToRepository {

    override suspend fun toRepository(keyword: String, startIndex: Int): BookEntity {
        return withContext(Dispatchers.IO){
            kakaoBookRemoteDataSource.getSearchKeyword(keyword, startIndex).toEntity().run {
                copy(items = items.filterNot { it.isbn.isNullOrEmpty() })
            }
        }
    }
}

class GoogleBooksDataSourceToRepositoryImpl (
    private val googleBooksRemoteDataSource: GoogleBooksRemoteDataSource
) : DataSourceToRepository {

    override suspend fun toRepository(keyword: String, startIndex: Int): BookEntity {
        return withContext(Dispatchers.IO){
            googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex).toEntity().run {
                copy(items = items.filterNot { it.isbn.isNullOrEmpty() })
            }
        }
    }
}