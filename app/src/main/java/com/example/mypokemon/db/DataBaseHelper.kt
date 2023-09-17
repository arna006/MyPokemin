package com.example.mypokemon.db

import androidx.room.Database
import androidx.room.RoomDatabase

//Creating Database and table
@Database(entities = [ScoreDataClass::class], version = 1)
abstract class DataBaseHelper : RoomDatabase() {
    //abstract function of Dao interface to do actions with database
    abstract fun dao(): DataBaseInterFace
}