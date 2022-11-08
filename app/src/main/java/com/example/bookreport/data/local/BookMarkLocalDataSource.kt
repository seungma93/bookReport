package com.example.bookreport.data.local

import android.content.Context
import android.util.Log
import com.example.bookreport.data.entity.room.BookMark
import com.example.bookreport.data.entity.room.BookMarkDatabase
import com.example.bookreport.data.entity.BookMarkEntity

interface BookMarkLocalDataSource{
    suspend fun insert(bookMark: BookMark)
    suspend fun delete(bookMark: BookMark)
    suspend fun select(): BookMarkEntity
}

class BookMarkLocalDataSourceImpl(private val context: Context): BookMarkLocalDataSource {
    private val db = BookMarkDatabase.getInstance(context.applicationContext)!!

    override suspend fun insert(bookMark: BookMark) {
        db.BookMarkDao().insert(bookMark)
    }

    override suspend fun delete(bookMark: BookMark) {
        db.BookMarkDao().delete(bookMark)
    }

    override suspend fun select(): BookMarkEntity {
        if(db.BookMarkDao().getAll().isNotEmpty()){
        for(element in db.BookMarkDao().getAll()) {
            Log.v("북마크 엔티티", element.title)
        }}else{
            Log.v("북마크 엔티티", "X")
        }

        return BookMarkEntity(db.BookMarkDao().getAll())
    }
}

