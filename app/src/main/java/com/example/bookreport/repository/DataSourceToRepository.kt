package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


interface DataSourceToRepository {
    suspend fun toRepository(keyword: String, page: Int): MutableStateFlow<BookEntity?>
}

class KakaoBookDataSourceToRepositoryImpl @Inject constructor(
    private val kakaoBookRemoteDataSource: KakaoBookRemoteDataSource
) : DataSourceToRepository {
    private val bookEntityStateFlow = MutableStateFlow<BookEntity?>(null)

    override suspend fun toRepository(keyword: String, page: Int): MutableStateFlow<BookEntity?> {

        kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
            bookEntityStateFlow.value = copy(items = items.filterNot { it.isbn.isEmpty() })
            return bookEntityStateFlow
        }
    }
}

class GoogleBooksDataSourceToRepositoryImpl @Inject constructor(
    private val googleBooksRemoteDataSource: GoogleBooksRemoteDataSource
) : DataSourceToRepository {
    private val bookEntityStateFlow = MutableStateFlow<BookEntity?>(null)

    override suspend fun toRepository(keyword: String, page: Int): MutableStateFlow<BookEntity?> {

        val startIndex = 0 + 10 * (page - 1)
        googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial").toEntity()
            .run {
                bookEntityStateFlow.value =
                    copy(items = items.filterNot { it.isbn.isEmpty() })
                return bookEntityStateFlow
            }
    }
}

