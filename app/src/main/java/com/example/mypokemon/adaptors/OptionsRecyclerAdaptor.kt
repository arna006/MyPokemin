package com.example.mypokemon.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mypokemon.databinding.OptionsitemlayoutBinding
import com.example.mypokemon.data.Result
import com.example.mypokemon.adaptors.viewholders.OptionsViewHolder

class OptionsRecyclerAdaptor(val context: Context) :
    RecyclerView.Adapter<OptionsViewHolder>() {
    private var mList: List<Result> = ArrayList()
    var onItemClick: ((Result) -> Unit?)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
        return OptionsViewHolder(
            OptionsitemlayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val singleItem = mList[position]
        with(holder.binding) {
            tvOption.text = singleItem.name
            mCard.setOnClickListener {
                onItemClick?.invoke(singleItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun submitList(newList: List<Result>) {
        mList = newList
        notifyDataSetChanged()
    }
}