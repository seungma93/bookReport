package com.example.bookreport.data.entity

import java.io.Serializable

sealed class BookEntity{
    data class KakaoBookEntity(
        val meta: KakaoBookMeta?,                // 책 메타데이터
        val documents: List<KakaoBookDocuments>          // 검색 결과
    ): BookEntity()
    data class GoogleBooksEntity(
        val meta: GoogleBooksMeta?,
        val items: List<GoogleBooksItems>
    ): BookEntity()
}


