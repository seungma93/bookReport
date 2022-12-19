package com.example.bookreport.repository

import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


interface KakaoBookRepository {
    suspend fun getBookRepository(keyword: String, page: Int): KakaoBookResultEntity
}

class KakaoBookRepositoryImpl @Inject constructor(
    private val bookRemoteDataSource: KakaoRemoteDataSource
) : KakaoBookRepository {
    override suspend fun getBookRepository(keyword: String, page: Int): KakaoBookResultEntity {
        val response = bookRemoteDataSource.getSearchKeyword(keyword, page)
        return if (response.documents.isEmpty()) response.toEntity() else response.toEntity()
    }
}