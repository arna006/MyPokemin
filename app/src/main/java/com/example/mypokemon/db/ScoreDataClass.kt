package com.example.mypokemon.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//creating entity with defining table name
@Entity(tableName = "scoretbl")
data class ScoreDataClass(
    @ColumnInfo(name = "score")
    var score: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0
}