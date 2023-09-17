package com.example.mypokemon.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypokemon.R
import com.example.mypokemon.adaptors.OptionsRecyclerAdaptor
import com.example.mypokemon.adaptors.PokemonRecyclerAdaptor
import com.example.mypokemon.baseclasses.BaseFragment
import com.example.mypokemon.databinding.FragmentSearchBinding
import com.example.mypokemon.enums.FilterType
import com.example.mypokemon.data.Result
import com.example.mypokemon.data.ResultDataClass
import com.example.mypokemon.enums.DataStates
import com.example.mypokemon.ui.activities.PokemonInfoActivity
import com.example.mypokemon.utils.DialogUtils
import com.example.mypokemon.utils.showToast
import com.example.mypokemon.utils.startNewActivity
import kotlinx.coroutines.flow.collectLatest

class SearchFragment : BaseFragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private var mDialogUtil: DialogUtils? = null
    private var optionsRecyclerAdaptor: OptionsRecyclerAdaptor? = null
    private var pokemonRecyclerAdaptor: PokemonRecyclerAdaptor? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        mDialogUtil = DialogUtils(requireContext())
        initRecycler()
        if (savedInstanceState == null) {
            updateUi(FilterType.BYTYPE)
        }
        with(binding) {
            btnMore.setOnClickListener {
                showMenu(it)
            }
            mSearch.setOnQueryTextListener(object :
                android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {

                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //calling search pokemon function then text change in search bar
                    searchPokemon(newText)

                    return true
                }

            })
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.filterOptions.collectLatest {
                when (it) {
                    is DataStates.Initial -> {}
                    is DataStates.Loading -> {
                        showProgressDialog("Getting options...")
                    }
                    is DataStates.Success<*> -> {
                        closeDialog()
                        //Displaying filter options
                        optionsRecyclerAdaptor?.submitList(
                            (it.value as ResultDataClass?)?.results ?: listOf()
                        )
                    }
                    is DataStates.Error -> {
                        closeDialog()
                        requireContext().showToast(it.message)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.filterPokemons.collectLatest {
                when (it) {
                    is DataStates.Initial -> {}
                    is DataStates.Loading -> {
                        showProgressDialog("Getting Pokemons...")
                    }
                    is DataStates.Success<*> -> {
                        closeDialog()
                        //Displaying Pokemons which are filtered
                        pokemonRecyclerAdaptor?.submitData(
                            (it.value as ArrayList<Result>?) ?: listOf()
                        )
                    }
                    is DataStates.Error -> {
                        closeDialog()
                        requireContext().showToast(it.message)
                    }
                }
            }
        }


    }

    private fun initRecycler() {
        //initializing recycler adaptors and setting up recyclerviews its adaptor and layout manager
        optionsRecyclerAdaptor = OptionsRecyclerAdaptor(requireContext())
        pokemonRecyclerAdaptor = PokemonRecyclerAdaptor(requireContext())
        binding.headerFilterTypeRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = optionsRecyclerAdaptor
        }
        binding.dataRecycler.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = pokemonRecyclerAdaptor
        }
        optionsRecyclerAdaptor?.onItemClick = { value ->
            //on Options click to show pokemons along the filter type option
            val mm = value.url.split('/')
            var end = mm[mm.lastIndex - 2]
            end = if (end == "type") {
                end + "/" + mm[mm.lastIndex - 1]
            } else {
                end + "/" + value.name
            }

            mViewModel.loadFilterPokemons(end)
            Unit
        }
        pokemonRecyclerAdaptor?.onItemClick = { result ->
            //navigate to next activity or page when click on pokemon
            requireActivity().startNewActivity<PokemonInfoActivity>(false) {
                it.putExtra("name", result.name)
            }
        }
    }

    private fun searchPokemon(newText: String?) {
        //searching pokemon with its name
        if (newText?.isEmpty() == true) {
            if (mViewModel.filterPokemons.value is DataStates.Success<*>) {
                val dd =
                    ((mViewModel.filterPokemons.value as DataStates.Success<*>).value as ArrayList<Result>?)
                pokemonRecyclerAdaptor?.submitData(dd ?: listOf())
            }
        } else {
            if (mViewModel.filterPokemons.value is DataStates.Success<*>) {
                val dd =
                    ((mViewModel.filterPokemons.value as DataStates.Success<*>).value as ArrayList<Result>?)
                pokemonRecyclerAdaptor?.submitData(dd?.filter {
                    it.name.lowercase().contains(newText?.lowercase() ?: "")
                } ?: listOf())
            }
        }
    }

    private fun showProgressDialog(message: String) {
        mDialogUtil?.showProgressDialog(message, false)
    }

    private fun closeDialog() {
        mDialogUtil?.dismissDialog()
    }


    private fun showMenu(view: View) {
        //showing popup menu on three dots click
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.filtertypes, popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btnByAbility -> {
                    updateUi(FilterType.BYABILITY)
                }
                R.id.btnByType -> {
                    updateUi(FilterType.BYTYPE)
                }
            }
            true
        }
    }

    private fun updateUi(filterType: FilterType) {
        binding.tvSearchBy.text = filterType.s
        mViewModel.loadFilterOptions(filterType)
    }
}