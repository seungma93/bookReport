package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource


interface GoogleBooksRepository {
    suspend fun getGoogleBooksEntity(keyword: String, startIndex: Int): BookEntity.GoogleBooksEntity
}

class GoogleBooksRepositoryImpl (
    private val googleBooksRemoteDataSource: GoogleBooksRemoteDataSource
) : GoogleBooksRepository {
    override suspend fun getGoogleBooksEntity(keyword: String, startIndex: Int): BookEntity.GoogleBooksEntity {
        val response = googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex)
        return if (response.items.isEmpty()) response.toEntity() else response.toEntity()
    }
}