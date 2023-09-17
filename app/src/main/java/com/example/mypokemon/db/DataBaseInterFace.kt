package com.example.mypokemon.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

//Dao interface
@Dao
interface DataBaseInterFace {
    //function to get Score
    @Query("Select * from scoretbl")
    fun getScore(): List<ScoreDataClass>

    //function to insert Score
    @Insert
    fun insert(value: ScoreDataClass): Long

}