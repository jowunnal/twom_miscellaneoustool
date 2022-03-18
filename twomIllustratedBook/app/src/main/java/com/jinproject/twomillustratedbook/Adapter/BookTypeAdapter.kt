package com.jinproject.twomillustratedbook.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.databinding.BookTypeItemBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener

class BookTypeAdapter(val context:Context) : RecyclerView.Adapter<BookTypeAdapter.ViewHolder>(),OnItemClickListener {
    var items=ArrayList<String>()
    var mlistener:OnItemClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BookTypeItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun setItem(items:ArrayList<String>){
        this.items=items
    }
    fun getItem(pos:Int):String{
        return items[pos]
    }

    inner class ViewHolder(private val binding: BookTypeItemBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(item:String){
            var imgName=""
            var itemName=""
            when(item){
                "miscellaneous"-> {
                    imgName = "etc_items"
                    itemName="잡탬류"
                }
                "weapons"-> {
                    imgName = "weapons"
                    itemName="무기류"
                }
                "costumes"-> {
                    imgName = "costumes"
                    itemName="코스튬류"
                }
            }
            val res=context.resources.getIdentifier(imgName,"drawable",context.packageName)
            binding.imageView3.setImageResource(res)
            binding.textView4.text=itemName
            binding.root.setOnClickListener {
                OnHomeItemClick(it,adapterPosition)
            }
        }
    }
    fun setOnItemClickListener(listener: OnItemClickListener){
        mlistener=listener
    }
    override fun OnHomeItemClick(v: View, pos: Int) {
        mlistener?.OnHomeItemClick(v,pos)
    }
}