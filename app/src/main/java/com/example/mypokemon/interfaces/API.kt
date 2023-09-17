package com.example.mypokemon.interfaces

import com.example.mypokemon.data.AbilityDataClass
import com.example.mypokemon.data.ResultDataClass
import com.example.mypokemon.data.AbilityTypeDataClass
import com.example.mypokemon.data.TypeDataClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface API {
    //Get all pokemon with pokemon?limit=100000&offset=0 end point
    @GET("pokemon?limit=100000&offset=0")
    fun getPokemons(): Call<ResultDataClass>

    //Get pokemon information by giving id
    @GET("pokemon/{id}")
    fun getPokemonInfo(
        @Path("id") id: String
    ): Call<AbilityDataClass>

    //Get Type filter options  by giving type
    @GET("{s}")
    fun getTypeOptions(@Path("s") s: String): Call<ResultDataClass>

    //Get pokemons by its type
    @GET("{s}")
    fun getPokemonsByType(@Path("s") s: String): Call<TypeDataClass>

    //Get pokemons by its ability
    @GET("{s}")
    fun getPokemonsByAbility(@Path("s") s: String): Call<AbilityTypeDataClass>

}