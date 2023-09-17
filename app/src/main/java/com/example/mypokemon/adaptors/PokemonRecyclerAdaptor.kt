package com.example.mypokemon.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokemon.databinding.PokemonitemlayoutBinding
import com.example.mypokemon.data.Result
import com.example.mypokemon.utils.setImage
import com.example.mypokemon.adaptors.viewholders.PokemonViewHolder

class PokemonRecyclerAdaptor(val context: Context) : RecyclerView.Adapter<PokemonViewHolder>() {
    var onItemClick: ((Result) -> Unit?)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            PokemonitemlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val singleItem = mList.currentList[position]
        with(holder.binding) {
            val kk = singleItem.url.split('/')
            val end = kk[kk.lastIndex - 1]
            val uu = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${
                end
            }.png"
            tvName.text = singleItem.name
            mImageView.setImage(uu)
            mCard.setOnClickListener {
                onItemClick?.invoke(singleItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.currentList.size
    }
    //creating diffUtil object
    private val differCallBack = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem.name == newItem.name

        }

        override fun areContentsTheSame(
            oldItem: Result,
            newItem: Result
        ): Boolean {
            return oldItem == newItem
        }
    }
    val mList = AsyncListDiffer(this, differCallBack)
    fun submitData(newList: List<Result>) {
        mList.submitList(newList)
    }
}