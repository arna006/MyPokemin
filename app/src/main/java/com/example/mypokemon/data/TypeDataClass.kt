package com.example.mypokemon.data


import com.google.gson.annotations.SerializedName

data class TypeDataClass(
    @SerializedName("id")
    val id: Int,
    @SerializedName("pokemon")
    val pokemon: List<Pokemon>
)