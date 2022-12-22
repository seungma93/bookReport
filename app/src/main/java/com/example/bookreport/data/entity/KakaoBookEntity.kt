package com.example.bookreport.data.entity

import java.io.Serializable

data class KakaoBookMeta(
    val totalCount: Int,
    val pageableCount: Int,
    val isEnd: Boolean
)

data class KakaoBookDocuments(
    val title: String,                      // 도서 제목
    val contents: String,                // 도서 소개
    val url: String,                          // 도서 상세 URL
    val isbn: String,                        // ISBN10(10자리) 또는 ISBN13(13자리) 형식의 국제 표준 도서번호(International Standard Book Number) ISBN10 또는 ISBN13 중 하나 이상 포함 두 값이 모두 제공될 경우 공백(' ')으로 구분
    val datetime: String,                // 도서 출판날짜, ISO 8601 형식 [YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
    val authors: List<String>,            // 도서 저자 리스트
    val publisher: String,              // 도서 출판사
    val translators: List<String>,    // 도서 제목
    val price: Int,                         // 도서 정가
    val salePrice: Int,               // 도서 판매가
    val thumbnail: String?,              // 도서 표지 미리보기 URL
    val status: String                    // 도서 판매 상태 정보 (정상, 품절, 절판 등) 상황에 따라 변동 가능성이 있으므로 문자열 처리 지양, 단순 노출 요소로 활용 권장
) : Serializable
