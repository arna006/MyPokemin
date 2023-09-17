package com.example.mypokemon.data


import com.google.gson.annotations.SerializedName

data class AbilityTypeDataClass(
    @SerializedName("id")
    val id: Int,
    @SerializedName("is_main_series")
    val isMainSeries: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("pokemon")
    val pokemon: List<Pokemon>
)