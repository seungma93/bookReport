package com.example.bookreport.domain

import com.example.bookreport.data.entity.KakaoBookResultEntity
import com.example.bookreport.repository.KakaoBookRepository
import com.example.bookreport.repository.KakaoBookRepositoryImpl

interface KakaoBookUseCase {
    suspend fun searchBook(keyword: String, page: Int): KakaoBookResultEntity
}

class KakaoBookUseCaseImpl(private val kakaoBookRepository: KakaoBookRepository) :
    KakaoBookUseCase {
    override suspend fun searchBook(keyword: String, page: Int): KakaoBookResultEntity {
        return kakaoBookRepository.getBookRepository(keyword, page)
    }
}