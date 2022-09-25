package com.jinproject.twomillustratedbook.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.Item.CheckStatue
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding
import com.jinproject.twomillustratedbook.listener.OnBossNameClickedListener
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import kotlin.properties.Delegates

class AlarmSelectedAdapter : RecyclerView.Adapter<AlarmSelectedAdapter.ViewHolder>(), OnBossNameClickedListener {
    private val items=ArrayList<String>()
    private val checkList=ArrayList<CheckStatue>()
    private lateinit var selectedList:ArrayList<CheckStatue>
    private var selectedStatue=-1
    private var mlistener:OnBossNameClickedListener ?= null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSelectedAdapter.ViewHolder {
        return ViewHolder(AlarmUserSelectedItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position],position)
    }

    fun setItems(items:ArrayList<String>){
        this.items.clear()
        this.items.addAll(items)
    }


    fun getItem(pos:Int):String{
        return items[pos]
    }


    inner class ViewHolder(private val binding: AlarmUserSelectedItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item:String,pos:Int){
            binding.alarmUserSelectedItem.text=item
            binding.alarmUserSelectedItem.setOnClickListener { // binding.root에 onclickListener를 등록하면, 뷰의 root에 리스너를 다는것으로 버튼에클릭이아닌, 버튼을제외한부분클릭이 되버림.
                setOnItemClickListener(it,adapterPosition,binding) // 각뷰홀더의 리스너로 리스너인터페이스 구현체인 어답터클래스 내부의 구현체메소드를 호출
                when(selectedStatue){
                    -1->{selectedList[pos].statue=true
                        binding.alarmUserSelectedItem.setTextColor(Color.RED)
                        selectedStatue=pos}
                    pos->{selectedList[pos].statue=false
                        binding.alarmUserSelectedItem.setTextColor(Color.BLACK)
                        selectedStatue=-1}
                }
            }
            if(pos>=selectedList.size){
                selectedList.add(pos,CheckStatue(false))
                Log.d("test",pos.toString())
            }


            if(selectedList[pos].statue){
                binding.alarmUserSelectedItem.setTextColor(Color.RED)
            }else{
                binding.alarmUserSelectedItem.setTextColor(Color.BLACK)
            }


            if(pos>=checkList.size){
                checkList.add(pos, CheckStatue(false))
            }

            binding.alarmUserSelectedItemSwitch.isChecked=checkList[pos].statue
            binding.alarmUserSelectedItemSwitch.setOnClickListener {
                when(binding.alarmUserSelectedItemSwitch.isChecked){
                    true->checkList[pos].statue=true
                    false->checkList[pos].statue=false
                }
            }

        }
    }

    fun setClickListener(listener:OnBossNameClickedListener){ //외부로부터 리스너객체를 받아 리스너객체 할당
        mlistener=listener
    }

    override fun setOnItemClickListener(v: View, pos: Int, binding: AlarmUserSelectedItemBinding) {
        // 34번줄에서 호출받아 외부로부터 등록된 리스너객체에 각각의 뷰홀더의 뷰와 위치값으로 리스너에 등록
        mlistener?.setOnItemClickListener(v,pos,binding)
    }
}