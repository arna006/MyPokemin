package com.example.mypokemon.mvvm.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mypokemon.data.AbilityDataClass
import com.example.mypokemon.db.DataBaseUtil
import com.example.mypokemon.db.ScoreDataClass
import com.example.mypokemon.enums.FilterType
import com.example.mypokemon.data.ResultDataClass
import com.example.mypokemon.interfaces.ResultCall
import com.example.mypokemon.data.QuizDataClass
import com.example.mypokemon.mvvm.repositories.DataRepositiory
import com.example.mypokemon.data.Result
import com.example.mypokemon.enums.DataStates
import com.example.mypokemon.data.AbilityTypeDataClass
import com.example.mypokemon.data.TypeDataClass
import com.example.mypokemon.utils.showLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    val dataRepositiory: DataRepositiory,
    val dataBaseUtil: DataBaseUtil
) : ViewModel() {
    private val _pokemones: MutableStateFlow<DataStates> = MutableStateFlow(DataStates.Initial)
    val pokemones = _pokemones.asStateFlow()

    private val _pokemoneInfo: MutableStateFlow<DataStates> = MutableStateFlow(DataStates.Initial)
    val pokemoneInfo = _pokemoneInfo.asStateFlow()

    private val _filterOptions: MutableStateFlow<DataStates> = MutableStateFlow(DataStates.Initial)
    val filterOptions = _filterOptions.asStateFlow()


    private val _filterPokemons: MutableStateFlow<DataStates> = MutableStateFlow(DataStates.Initial)
    val filterPokemons = _filterPokemons.asStateFlow()

    private val _score: MutableLiveData<ScoreDataClass> = MutableLiveData()
    val score: LiveData<ScoreDataClass> = _score


    private val _quizData: MutableLiveData<QuizDataClass> = MutableLiveData()
    val quizData: LiveData<QuizDataClass> = _quizData

    fun loadPokemons() = viewModelScope.launch(Dispatchers.IO) {
        _pokemones.value = DataStates.Loading
        dataRepositiory.loadPokemones(object : ResultCall<Any?>() {
            override fun resultSuccess(result: Any?) {
                super.resultSuccess(result)
                _pokemones.value = DataStates.Success(result as ResultDataClass?)

            }

            override fun onFail(message: String) {
                super.onFail(message)
                _pokemones.value = DataStates.Error(message)

            }
        })
    }

    fun loadPokemonInfo(id: String) = viewModelScope.launch(Dispatchers.IO) {
        _pokemoneInfo.value = DataStates.Loading
        dataRepositiory.loadPokemoneInfo(id, object : ResultCall<Any?>() {
            override fun resultSuccess(result: Any?) {
                super.resultSuccess(result)
                _pokemoneInfo.value = DataStates.Success(result as AbilityDataClass)

            }

            override fun onFail(message: String) {
                super.onFail(message)
                _pokemoneInfo.value = DataStates.Error(message)

            }
        })
    }

    fun loadFilterOptions(filterType: FilterType) = viewModelScope.launch(Dispatchers.IO) {
        _filterOptions.value = DataStates.Loading
        dataRepositiory.loadFilterOptions(filterType, object : ResultCall<Any?>() {
            override fun resultSuccess(result: Any?) {
                super.resultSuccess(result)
                _filterOptions.value = DataStates.Success(result)

            }

            override fun onFail(message: String) {
                super.onFail(message)
                _filterOptions.value = DataStates.Error(message)

            }
        })
    }

    fun loadFilterPokemons(endpoint: String) = viewModelScope.launch(Dispatchers.IO) {
        _filterPokemons.value = DataStates.Loading
        dataRepositiory.loadFilterPokemons(endpoint, object : ResultCall<Any?>() {
            override fun resultSuccess(result: Any?) {
                super.resultSuccess(result)
                val list: ArrayList<Result> = ArrayList()
                if (result is AbilityTypeDataClass?) {
                    "dataClick".showLog("Ability")
                    result?.pokemon?.forEach {
                        list.add(Result(it.pokemon.name, it.pokemon.url))
                    }
                } else if (result is TypeDataClass?) {
                    "dataClick".showLog("Type")
                    result?.pokemon?.forEach {
                        list.add(Result(it.pokemon.name, it.pokemon.url))
                    }
                }
                _filterPokemons.value = DataStates.Success(list)
            }

            override fun onFail(message: String) {
                super.onFail(message)
                _filterPokemons.value = DataStates.Error(message)

            }
        })
    }

    fun loadMyScore() = viewModelScope.launch(Dispatchers.IO) {
        _score.postValue(dataBaseUtil.getScore())
    }

    fun setQuiz(value: QuizDataClass) = viewModelScope.launch {
        _quizData.value = value
    }

    fun addScore(newScore: Int?) = viewModelScope.launch(Dispatchers.IO) {
        dataBaseUtil.insertScore(ScoreDataClass(newScore!!))
        loadMyScore()
    }
}