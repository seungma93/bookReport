package com.example.bookreport.data.entity

import java.io.Serializable


sealed class BookEntity {

    abstract val items: List<Documents>
    abstract val meta: Meta

    data class KakaoBookEntity(
        override val meta: Meta.KakaoBookMeta,                // 책 메타데이터
        override val items: List<Documents>          // 검색 결과
    ): BookEntity()

    data class GoogleBooksEntity(
        override val meta: Meta.GoogleBooksMeta,
        override val items: List<Documents>
    ): BookEntity()
}


sealed class  Meta {
    abstract val totalCount: Int
    abstract val isEnd: Boolean

    data class KakaoBookMeta(
        override val totalCount: Int,
        override val isEnd: Boolean
    ): Meta()

    data class GoogleBooksMeta(
        override val totalCount: Int,
        val bookItemSize: Int
    ): Meta() {
        companion object {
            const val DEFAULT_PAGE_SIZE = 10
        }
        override val isEnd: Boolean get() = bookItemSize < DEFAULT_PAGE_SIZE
    }
}

data class Documents(
     val title: String,
     val contents: String,
     val url: String,
     val isbn: String,
     val datetime: String,
     val authors: List<String>,
     val publisher: String,
     val thumbnail: String?
) : Serializable


/*
sealed class Documents {
    abstract val title: String                      // 도서 제목
    abstract val contents: String                // 도서 소개
    abstract val url: String                          // 도서 상세 URL
    abstract val isbn: String                        // ISBN10(10자리) 또는 ISBN13(13자리) 형식의 국제 표준 도서번호(International Standard Book Number) ISBN10 또는 ISBN13 중 하나 이상 포함 두 값이 모두 제공될 경우 공백(' ')으로 구분
    abstract val datetime: String                // 도서 출판날짜, ISO 8601 형식 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    abstract val authors: List<String>            // 도서 저자 리스트
    abstract val publisher: String              // 도서 출판사
    abstract val thumbnail: String?              // 도서 표지 미리보기 URL

    data class KakaoBookDocuments(
        override val title: String,
        override val contents: String,
        override val url: String,
        override val isbn: String,
        override val datetime: String,
        override val authors: List<String>,
        override val publisher: String,
        override val thumbnail: String?
    ) : Serializable, Documents()

    data class GoogleBooksDocuments(
        override val title: String,
        override val contents: String,
        override val url: String,
        override val isbn: String,
        override val datetime: String,
        override val authors: List<String>,
        override val publisher: String,
        override val thumbnail: String?
    ) : Serializable, Documents()
}

 */
