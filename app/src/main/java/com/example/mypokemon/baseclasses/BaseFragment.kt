package com.example.mypokemon.baseclasses

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mypokemon.mvvm.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


//BaseClass of Fragment which is extended with derived class
open class BaseFragment(val layoutId: Int) : Fragment(layoutId) {
    protected val mViewModel: MainViewModel by sharedViewModel()
}