package com.jinproject.twomillustratedbook.ui.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.domain.Item.HomeItem
import com.jinproject.twomillustratedbook.databinding.HomeItemBinding
import com.jinproject.twomillustratedbook.ui.listener.OnItemClickListener

class HomeAdapter() : RecyclerView.Adapter<HomeAdapter.ViewHolder>() , OnItemClickListener {
    var items = ArrayList<HomeItem>()
    var mlistener : OnItemClickListener?=null

    inner class ViewHolder(private val binding:HomeItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : HomeItem){
            binding.homeItemImg.setImageResource(item.homeImgRes)
            binding.homeItemName.text=item.homeName
            binding.root.setOnClickListener {
                mlistener?.OnHomeItemClick(it,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HomeItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(pos:Int): HomeItem {
        return items[pos]
    }

    fun addItems(items : ArrayList<HomeItem>){
        this.items=items
    }

    override fun OnHomeItemClick(v: View, pos: Int) {
        mlistener?.OnHomeItemClick(v,pos)
    }

    fun setItemClickListener(listener: OnItemClickListener){
        mlistener=listener
    }

}