package com.example.mypokemon.appclass

import android.app.Application
import com.example.mypokemon.db.DataBaseUtil
import com.example.mypokemon.mvvm.repositories.DataRepositiory
import com.example.mypokemon.mvvm.viewmodels.MainViewModel
import com.example.mypokemon.api.WebClient
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module


class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        //initializing koin in application class
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@AppClass)
            modules(
                listOf(
                    appModule
                )
            )
        }

    }

    private val appModule = module {
        single { WebClient(androidContext()) }
        viewModel {
            MainViewModel(
                DataRepositiory(androidContext(), get()),
                DataBaseUtil(androidContext())
            )
        }
    }
}