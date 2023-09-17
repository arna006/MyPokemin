package com.example.mypokemon.interfaces

// call back methods
abstract class ResultCall<T> {
    open fun onFail(message: String) {}

    open fun resultSuccess(result: T) {}
}