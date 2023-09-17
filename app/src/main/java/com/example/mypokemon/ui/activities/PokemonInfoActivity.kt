package com.example.mypokemon.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.text.HtmlCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypokemon.data.*
import com.example.mypokemon.adaptors.ImageRecyclerAdaptor
import com.example.mypokemon.baseclasses.BaseActivity
import com.example.mypokemon.databinding.ActivityPokemonInfoBinding
import com.example.mypokemon.enums.DataStates
import com.example.mypokemon.mvvm.viewmodels.MainViewModel
import com.example.mypokemon.utils.DialogUtils
import com.example.mypokemon.utils.beGone
import com.example.mypokemon.utils.setImage
import com.example.mypokemon.utils.showToast

import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel


class PokemonInfoActivity : BaseActivity() {
    //creating objects of classes
    private lateinit var binding: ActivityPokemonInfoBinding
    private var name: String? = null
    private var mDialogUtil: DialogUtils? = null
    private val mainViewModel: MainViewModel by viewModel()
    private var mImageRecyclerAdaptor: ImageRecyclerAdaptor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        name = intent.extras?.getString("name")
        initRecycler()
        with(binding) {
            mHeaderLayout.tvScore.beGone()
            mHeaderLayout.tvHeader.text = name
        }
        mDialogUtil = DialogUtils(this)
        name?.let {
            if (savedInstanceState == null) {
                mainViewModel.loadPokemonInfo(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            mainViewModel.pokemoneInfo.collectLatest {
                when (it) {
                    is DataStates.Initial -> {
                    }
                    is DataStates.Loading -> {
                        //showing progress
                        showProgressDialog()
                    }
                    is DataStates.Success<*> -> {
                        //Displaying Data onto screen
                        hideProgressDialog()
                        bindData(it.value as AbilityDataClass?)
                    }
                    is DataStates.Error -> {
                        //showing error if any error occure
                        showToast(it.message)
                        hideProgressDialog()
                    }
                }
            }
        }
    }

    private fun initRecycler() {
        //initializing recycler adaptor and setting up recyclerview its adaptor and layout manager
        mImageRecyclerAdaptor = ImageRecyclerAdaptor(this)
        binding.imagesRecycler.apply {
            layoutManager =
                LinearLayoutManager(this@PokemonInfoActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mImageRecyclerAdaptor
        }
    }

    private fun showProgressDialog() {
        mDialogUtil?.showProgressDialog("Getting Info", false)
    }

    private fun hideProgressDialog() {
        mDialogUtil?.dismissDialog()
    }

    private fun bindData(data: AbilityDataClass?) {
        data?.let {
            with(binding) {
                mImageRecyclerAdaptor?.submitList(getImages(data.sprites))
                pokiImage.setImage(data.sprites.frontDefault)
                tvPokiInfo.text = getPokiInfoText(data)
                tvAbilities.text = getPokiAbilites(data.abilities)
                tvForms.text = getPokiForms(data.forms)
                tvMoves.text = getPokiMoves(data.moves)

            }
        }
    }

    private fun getPokiMoves(moves: List<Move>): String? {
        val stringBuilder = StringBuilder()
        moves.forEachIndexed { index, move ->
            stringBuilder.append(
                "${move.move.name}, "
            )


        }
        return stringBuilder.toString()
    }

    private fun getPokiForms(forms: List<Form>): String? {
        val stringBuilder = StringBuilder()
        forms.forEachIndexed { index, form ->
            stringBuilder.append(
                "${form.name}, "
            )


        }
        return stringBuilder.toString()
    }

    private fun getPokiAbilites(abilities: List<Ability>): String? {
        val stringBuilder = StringBuilder()
        abilities.forEachIndexed { index, ability ->
            stringBuilder.append(
                "${ability.ability.name}, "
            )

        }
        return stringBuilder.toString()
    }

    private fun getImages(sprites: Sprites): List<String> {
        val mlist = listOf(
            sprites.frontDefault,
            sprites.backDefault,
            sprites.backShiny,
            sprites.frontShiny,
            sprites.versions.generationI.redBlue.backDefault,
            sprites.versions.generationI.redBlue.backGray,
            sprites.versions.generationI.redBlue.backTransparent,
            sprites.versions.generationI.redBlue.frontDefault,
            sprites.versions.generationI.redBlue.frontGray,
            sprites.versions.generationI.redBlue.frontTransparent,
            sprites.versions.generationI.yellow.backDefault,
            sprites.versions.generationI.yellow.backGray,
            sprites.versions.generationI.yellow.backTransparent,
            sprites.versions.generationI.yellow.frontDefault,
            sprites.versions.generationI.yellow.frontGray,
            sprites.versions.generationI.yellow.frontTransparent,
        )
        return mlist
    }

    private fun getPokiInfoText(data: AbilityDataClass): String {
        val stringBuilder: StringBuilder = java.lang.StringBuilder()
        stringBuilder.apply {
            append(
                HtmlCompat.fromHtml(
                    "<b>Pokemon Name: </b>${data.name}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
            append('\n')
            append(
                HtmlCompat.fromHtml(
                    "<b>Height: </b>${data.height}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
            append('\n')
            append(
                HtmlCompat.fromHtml(
                    "<b>Weight: </b>${data.weight}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
            append('\n')
            append(
                HtmlCompat.fromHtml(
                    "<b>Base Experience: </b>${data.baseExperience}",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
        }

        return stringBuilder.toString()
    }
}