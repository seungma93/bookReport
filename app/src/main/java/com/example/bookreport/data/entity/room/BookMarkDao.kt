package com.example.bookreport.data.entity.room

import androidx.room.*

@Dao
interface BookMarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookMark: BookMark)

    @Update
    suspend fun update(bookMark: BookMark)

    @Delete
    suspend fun delete(bookMark: BookMark)



    @Query("SELECT * FROM BookMark")
    suspend fun getAll(): List<BookMark>
/*
    @Query("DELETE FROM User ")
    suspend fun deleteAll()
     */
}

@Dao
interface GoogleBooksBookMarkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookMark: GoogleBooksBookMark)

    @Update
    suspend fun update(bookMark: GoogleBooksBookMark)

    @Delete
    suspend fun delete(bookMark: GoogleBooksBookMark)



    @Query("SELECT * FROM GoogleBooksBookMark")
    suspend fun getAll(): List<GoogleBooksBookMark>
/*
    @Query("DELETE FROM User ")
    suspend fun deleteAll()
     */
}