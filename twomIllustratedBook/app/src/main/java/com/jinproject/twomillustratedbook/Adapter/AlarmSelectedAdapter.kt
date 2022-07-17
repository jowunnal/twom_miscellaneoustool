package com.jinproject.twomillustratedbook.Adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener

class AlarmSelectedAdapter : RecyclerView.Adapter<AlarmSelectedAdapter.ViewHolder>(), OnItemClickListener {
    private val items=ArrayList<String>()
    private var mlistener:OnItemClickListener ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSelectedAdapter.ViewHolder {
        return ViewHolder(AlarmUserSelectedItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items:ArrayList<String>){
        this.items.clear()
        this.items.addAll(items)
    }

    inner class ViewHolder(private val binding: AlarmUserSelectedItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item:String){
            binding.alarmUserSelectedItem.text=item
            binding.root.setOnClickListener {
                OnHomeItemClick(it,adapterPosition) // 각뷰홀더의 리스너로 리스너인터페이스 구현체인 어답터클래스 내부의 구현체메소드를 호출
                binding.alarmUserSelectedItem.setBackgroundColor(Color.RED)
            }
        }
    }

    fun setOnItemClickListener(listener:OnItemClickListener){ //외부로부터 리스너객체를 받아 리스너객체 할당
        mlistener=listener
    }

    override fun OnHomeItemClick(v: View, pos: Int) { // 34번줄에서 호출받아 외부로부터 등록된 리스너객체에 각각의 뷰홀더의 뷰와 위치값으로 리스너에 등록
        mlistener?.OnHomeItemClick(v,pos)
    }
}