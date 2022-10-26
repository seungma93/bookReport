package com.example.bookreport.repository

import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface KakaoBookRepository{
    suspend fun getBookRepository(keyword: String, page: Int): KakaoBookResultEntity
}

class KakaoBookRepositoryImpl(private val bookRemoteDataSource : KakaoRemoteDataSource) : KakaoBookRepository {
    override suspend fun getBookRepository(keyword: String, page: Int) : KakaoBookResultEntity{
       return withContext(Dispatchers.IO){
           val response = bookRemoteDataSource.getSearchKeyword(keyword, page)
           withContext(Dispatchers.Main) {
               if (response.documents.isEmpty()) response.toEntity() else response.toEntity()
           }
       }
    }
}