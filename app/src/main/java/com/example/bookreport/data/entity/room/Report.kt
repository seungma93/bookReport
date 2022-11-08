package com.example.bookreport.data.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Report(
    var title: String,
    var thumbnail : String,
    var context: String
) {
    @PrimaryKey(autoGenerate = true)
    var no = 0
}


