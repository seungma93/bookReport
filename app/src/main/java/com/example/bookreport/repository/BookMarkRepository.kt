package com.example.bookreport.repository

import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.local.BookMarkLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface BookMarkRepository {
    suspend fun insertData(bookMark: BookMark)
    suspend fun deleteData(bookMark: BookMark)
    suspend fun selectData(): BookMarkEntity
}

class BookMarkRepositoryImpl (
    private val bookMarkLocalDataSource: BookMarkLocalDataSource,
) : BookMarkRepository {
    override suspend fun insertData(bookMark: BookMark) {
            bookMarkLocalDataSource.insert(bookMark)
    }

    override suspend fun deleteData(bookMark: BookMark) {
            bookMarkLocalDataSource.delete(bookMark)
    }

    override suspend fun selectData(): BookMarkEntity {
            return bookMarkLocalDataSource.select()
    }

}