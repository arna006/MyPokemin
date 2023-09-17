package com.example.mypokemon.db

import android.content.Context
import androidx.room.Room


class DataBaseUtil(val context: Context) {

    fun getScore(): ScoreDataClass? {
        val list = mDataBaseHelper?.dao()?.getScore()
        return if (list?.isNotEmpty() == true) {
            list.last()
        } else {
            val ll = insertScore(ScoreDataClass(0))
            ScoreDataClass(0).also { it.id = ll?.toInt() ?: 0 }
        }
    }

    fun insertScore(value: ScoreDataClass): Long? {
        return mDataBaseHelper?.dao()?.insert(value)
    }

    private var mDataBaseHelper: DataBaseHelper? = null

    init {
        mDataBaseHelper = Room.databaseBuilder(context, DataBaseHelper::class.java, "AppDataBase")
            .fallbackToDestructiveMigration()
            .build()
    }


}