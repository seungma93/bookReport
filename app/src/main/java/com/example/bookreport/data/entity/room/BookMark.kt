package com.example.bookreport.data.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookMark(
    @PrimaryKey
    val title: String
)

@Entity
data class GoogleBooksBookMark(
    @PrimaryKey
    val title: String
)