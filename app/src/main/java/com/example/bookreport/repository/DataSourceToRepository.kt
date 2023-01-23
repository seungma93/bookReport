package com.example.bookreport.repository

import com.example.bookreport.data.entity.BookEntity
import com.example.bookreport.data.entity.BookListEntity
import com.example.bookreport.data.entity.Meta
import com.example.bookreport.data.mapper.toEntity
import com.example.bookreport.data.remote.GoogleBooksRemoteDataSource
import com.example.bookreport.data.remote.KakaoBookRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


interface DataSourceToRepository {
    suspend fun getDataSource(keyword: String, page: Int): Flow<BookEntity?>
    suspend fun getNewDataSource(keyword: String, page: Int): Flow<BookEntity?>
    fun subscribeDataSource(): Flow<BookEntity?>
}

class KakaoBookDataSourceToRepositoryImpl (
    private val kakaoBookRemoteDataSource: KakaoBookRemoteDataSource
) : DataSourceToRepository {
    private val bookEntityStateFlow = MutableStateFlow<BookEntity?>(null)

    override suspend fun getDataSource(keyword: String, page: Int): Flow<BookEntity?> {
            val oldList = bookEntityStateFlow.value as BookEntity.KakaoBookEntity

            kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
                val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                bookEntityStateFlow.value =
                    BookEntity.KakaoBookEntity(newList.meta, oldList.items + newList.items)
                return bookEntityStateFlow
            }
    }

    override suspend fun getNewDataSource(keyword: String, page: Int): Flow<BookEntity?> {
        bookEntityStateFlow.value = null
        kakaoBookRemoteDataSource.getSearchKeyword(keyword, page).toEntity().run {
            val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
            bookEntityStateFlow.value =
                BookEntity.KakaoBookEntity(newList.meta, newList.items)
            return bookEntityStateFlow
        }
    }
    override fun subscribeDataSource(): Flow<BookEntity?> = bookEntityStateFlow
}

class GoogleBooksDataSourceToRepositoryImpl (
    private val googleBooksRemoteDataSource: GoogleBooksRemoteDataSource
) : DataSourceToRepository {
    private val bookEntityStateFlow = MutableStateFlow<BookEntity?>(null)

    override suspend fun getDataSource(keyword: String, page: Int): Flow<BookEntity?> {
        val oldList = bookEntityStateFlow.value as BookEntity.GoogleBooksEntity
        val startIndex = 0 + 10 * (page - 1)
        googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial").toEntity()
            .run {
                val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                bookEntityStateFlow.value =
                    BookEntity.GoogleBooksEntity(newList.meta, oldList.items + newList.items)

                return bookEntityStateFlow
            }
    }

    override suspend fun getNewDataSource(keyword: String, page: Int): Flow<BookEntity?> {
        bookEntityStateFlow.value = null
        val startIndex = 0 + 10 * (page - 1)
        googleBooksRemoteDataSource.getSearchKeyword(keyword, startIndex, "partial").toEntity()
            .run {
                val newList = copy(items = items.filterNot { it.isbn.isEmpty() })
                bookEntityStateFlow.value =
                    BookEntity.GoogleBooksEntity(newList.meta, newList.items)
                return bookEntityStateFlow
            }
    }

    override fun subscribeDataSource(): Flow<BookEntity?> = bookEntityStateFlow
}

