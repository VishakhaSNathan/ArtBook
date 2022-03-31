package com.example.artbook.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arts")
data class Art (
    var Artname: String,
    var ArtistName: String,
    var year: Int,
    var description: String,
    var imageUrl : String,
    @PrimaryKey(autoGenerate = true)
    var id: Int?=null
)