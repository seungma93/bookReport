package com.example.bookreport.data.entity.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity
data class Report(
    var title: String,
    var thumbnail : String,
    var context: String
): Serializable {
    @PrimaryKey(autoGenerate = true)
    var no = 0
}


