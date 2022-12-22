package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import javax.inject.Inject


interface KakaoBookRepository {
    suspend fun getBookRepository(keyword: String, page: Int): BookEntity.KakaoBookEntity
}

class KakaoBookRepositoryImpl @Inject constructor(
    private val bookRemoteDataSource: KakaoRemoteDataSource
) : KakaoBookRepository {
    override suspend fun getBookRepository(keyword: String, page: Int): BookEntity.KakaoBookEntity {
        val response = bookRemoteDataSource.getSearchKeyword(keyword, page)
        return if (response.documents.isEmpty()) response.toEntity() else response.toEntity()
    }
}