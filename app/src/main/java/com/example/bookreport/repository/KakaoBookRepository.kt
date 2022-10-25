package com.example.bookreport.repository

import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.KakaoRemoteDataSource
import com.example.bookreport.data.remote.KakaoRemoteDataSourceImpl
import com.example.bookreport.network.BookRetrofit

interface KakaoBookRepository{
    suspend fun getBookRepository(keyword: String, page: Int): KakaoBookResultEntity
}

class KakaoBookRepositoryImpl(private val bookRemoteDataSource : KakaoRemoteDataSource) : KakaoBookRepository {

    override suspend fun getBookRepository(keyword: String, page: Int): KakaoBookResultEntity {
        val bookRemoteDataSourceImpl = bookRemoteDataSource.getSearchBook(keyword, page)
        if( bookRemoteDataSourceImpl.documents.isEmpty()){
            return bookRemoteDataSourceImpl.toEntity()
        }else{
            return bookRemoteDataSourceImpl.toEntity()
        }
    }

}