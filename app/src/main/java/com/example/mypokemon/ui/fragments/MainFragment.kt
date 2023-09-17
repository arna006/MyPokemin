package com.example.mypokemon.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypokemon.R
import com.example.mypokemon.adaptors.PokemonRecyclerAdaptor
import com.example.mypokemon.baseclasses.BaseFragment
import com.example.mypokemon.databinding.FragmentMainBinding
import com.example.mypokemon.data.ResultDataClass
import com.example.mypokemon.enums.DataStates
import com.example.mypokemon.ui.activities.PokemonInfoActivity
import com.example.mypokemon.utils.beGone
import com.example.mypokemon.utils.beVisible
import com.example.mypokemon.utils.showToast
import com.example.mypokemon.utils.startNewActivity

import kotlinx.coroutines.flow.collectLatest


class MainFragment : BaseFragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding
    private var mAdaptor: PokemonRecyclerAdaptor? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        if (savedInstanceState == null) {
            //loading pokemons data and score
            loadPokemons()
            loadScore()
        }
        with(binding) {
            root.setOnRefreshListener {
                loadPokemons()
            }
            mHeaderLayout.tvHeader.text = getString(R.string.app_name)
        }
        initRecycler()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.pokemones.collectLatest {
                when (it) {
                    is DataStates.Initial -> {}
                    is DataStates.Loading -> {
                        showLoading()
                        hideRecycler()
                    }
                    is DataStates.Success<*> -> {
                        hideLoading()
                        showRecycler()
                        mAdaptor?.submitData((it.value as ResultDataClass?)?.results ?: listOf())
                    }
                    is DataStates.Error -> {
                        hideLoading()
                        hideRecycler()
                        requireContext().showToast(it.message)
                    }
                }
            }
        }
        mViewModel.score.observe(viewLifecycleOwner) {
            binding.mHeaderLayout.tvScore.text = "Score : ${it.score}"
        }
    }

    private fun loadScore() {
        mViewModel.loadMyScore()
    }

    private fun initRecycler() {
        //initializing recycler adaptor and setting up recyclerview its adaptor and layout manager

        mAdaptor = PokemonRecyclerAdaptor(requireContext())
        binding.mRecycler.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = mAdaptor

        }
        mAdaptor?.onItemClick = { result ->
            //On pokemon click navigation on next page or activity
            requireActivity().startNewActivity<PokemonInfoActivity>(false) {
                it.putExtra("name", result.name)
            }
        }


    }

    private fun showLoading() {
        binding.root.isRefreshing = true
    }

    private fun hideLoading() {
        binding.root.isRefreshing = false
    }

    private fun showRecycler() {
        binding.mRecycler.beVisible()

    }

    private fun hideRecycler() {
        binding.mRecycler.beGone()
    }

    private fun loadPokemons() {
        mViewModel.loadPokemons()
    }

}