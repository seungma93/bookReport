package com.example.bookreport.data.mapper

import com.example.bookreport.BookMetaResponse
import com.example.bookreport.BookResponse
import com.example.bookreport.ResultSearchKeywordResponse
import com.example.bookreport.data.entity.KakaoBook
import com.example.bookreport.data.entity.KakaoBookMeta
import com.example.bookreport.data.entity.KakaoBookResultEntity

fun ResultSearchKeywordResponse.toEntity() = KakaoBookResultEntity(
    meta = meta.toEntity(),
    documents = documents.map{
        it.toEntity()
    }
)

fun BookMetaResponse.toEntity() = KakaoBookMeta(
    totalCount = totalCount ?: 0,
    pageableCount = pageableCount ?: 0,
    isEnd = isEnd ?: true
)

fun BookResponse.toEntity() = KakaoBook(
    title = title.orEmpty(),
    contents = contents.orEmpty(),
    url = url.orEmpty(),
    isbn = isbn.orEmpty(),
    datetime = datetime.orEmpty(),
    authors = authors.orEmpty(),
    publisher = publisher.orEmpty(),
    translators = translators.orEmpty(),
    price = price ?:0,
    salePrice = sale_price ?:0,
    thumbnail = thumbnail.orEmpty(),
    status = status.orEmpty()
)