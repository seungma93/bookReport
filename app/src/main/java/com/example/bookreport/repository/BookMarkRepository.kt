package com.example.bookreport.repository

import android.util.Log
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.BookMarkEntity
import com.example.bookreport.data.local.BookMarkLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface BookMarkRepository{
    suspend fun insertData(bookMark: BookMark)
    suspend fun deleteData(bookMark: BookMark)
    suspend fun selectData(): BookMarkEntity
}

class BookMarkRepositoryImpl @Inject constructor(private val bookMarkLocalDataSource: BookMarkLocalDataSource):BookMarkRepository {
    override suspend fun insertData(bookMark: BookMark) {
        withContext(Dispatchers.IO) {
            bookMarkLocalDataSource.insert(bookMark)
        }
    }

    override suspend fun deleteData(bookMark: BookMark) {
        withContext(Dispatchers.IO) {
            bookMarkLocalDataSource.delete(bookMark)
        }
    }

    override suspend fun selectData(): BookMarkEntity {
        return withContext(Dispatchers.IO) {
            bookMarkLocalDataSource.select()
        }
    }

}