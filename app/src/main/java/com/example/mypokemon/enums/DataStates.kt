package com.example.mypokemon.enums

//Sealed class work like enum class where we can define states
sealed class DataStates {
    object Loading : DataStates()
    object Initial : DataStates()
    class Success<T>(val value: T) : DataStates()
    class Error(val message: String) : DataStates()
}
