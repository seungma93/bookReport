package com.example.bookreport

import com.google.gson.annotations.SerializedName

data class ResultSearchKeywordResponse(
    val meta: BookMetaResponse,                // 책 메타데이터
    val documents: List<BookResponse>          // 검색 결과
)

data class BookMetaResponse(
    @SerializedName("total_count") val totalCount: Int?,
    @SerializedName("pageable_count") val pageableCount: Int?,
    @SerializedName("is_end") val isEnd: Boolean?
)

data class BookResponse(
    @SerializedName("title") val title: String? = null,                      // 도서 제목
    @SerializedName("contents") val contents: String? = null,                // 도서 소개
    @SerializedName("url") val url: String? = null,                          // 도서 상세 URL
    @SerializedName("isbn") val isbn: String? = null,                        // ISBN10(10자리) 또는 ISBN13(13자리) 형식의 국제 표준 도서번호(International Standard Book Number) ISBN10 또는 ISBN13 중 하나 이상 포함 두 값이 모두 제공될 경우 공백(' ')으로 구분
    @SerializedName("datetime") val datetime: String? = null,                // 도서 출판날짜, ISO 8601 형식 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    @SerializedName("authors") val authors: List<String>? = null,            // 도서 저자 리스트
    @SerializedName("publisher") val publisher: String? = null,              // 도서 출판사
    @SerializedName("translators") val translators: List<String>? = null,    // 도서 제목
    @SerializedName("price") val price: Int? = null,                         // 도서 정가
    @SerializedName("sale_price") val sale_price: Int? = null,               // 도서 판매가
    @SerializedName("thumbnail") val thumbnail: String? = null,              // 도서 표지 미리보기 URL
    @SerializedName("status") val status: String? = null                   // 도서 판매 상태 정보 (정상, 품절, 절판 등) 상황에 따라 변동 가능성이 있으므로 문자열 처리 지양, 단순 노출 요소로 활용 권장
)