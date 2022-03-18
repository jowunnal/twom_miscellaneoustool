package com.jinproject.twomillustratedbook.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.databinding.DroplistmapItemBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener

class DropListMapAdapter(val context: Context) : RecyclerView.Adapter<DropListMapAdapter.ViewHolder>(),OnItemClickListener{
    var items=ArrayList<String>()
    var mlistener : OnItemClickListener ?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DroplistmapItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(item:List<String>){
        items.addAll(item)
    }

    fun getItem(pos:Int):String{
        return items[pos]
    }

    inner class ViewHolder(private val binding:DroplistmapItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String){
            binding.dropMapName.text=item
            var imgName=""
            when(item){
                "우디위디숲" -> imgName="bulldozerbro"
                "버섯늪지" -> imgName="fugusking"
                "머쉬룸스포어" -> imgName="poison_fugusking"
                "윙프릴섬해변" -> imgName="chief_wooparoopa"
                "등대던전1층" -> imgName="recluse"
                "등대던전2층" -> imgName="awaken_kooii"
                "등대던전3층" -> imgName="green_ghost"
                "등대던전4층" -> imgName="guardian_imp"
                "등대던전5층" -> imgName="devilang"
                "해적선" -> imgName="blanka"
                "빛이들지않는신전" -> imgName="wadangka"
                "해지는노을숲" -> imgName="tailfox777"
                "라노스평원" -> imgName="whitecrow"
                "폐허가있는숲" -> imgName="ghostsnake"
                "카타르산맥" -> imgName="magma_chief_thief"
                "모래무덤골짜기" -> imgName="shaaack"
                "건조한초원" -> imgName="bssszsss"
                "뜨거운모래사막" -> imgName="bigmama"
                "고대의누각" -> imgName="puppet"
                "하늘성채-서부" -> imgName="tyrant"
                "하늘성채-동부" -> imgName="twister"
                "돌무더기 요새" -> imgName="ominous_bird"
                "알수없는 미로" -> imgName="steampunk"
                "타락한 신전" -> imgName="caligo"
                "오염된 숲" -> imgName="forest_keeper"
                "죽음의 늪" -> imgName="ukpana"
                "숲의 미궁" -> imgName="darlene"
                "이슬롯의 실험실" -> imgName="mutated_sharkun"
                "이슬롯의 신전" -> imgName="mutated_shurun"
                "루나프" -> imgName="sephia"
                "비탄의제단" -> imgName="illust"
            }
            val res=context.resources.getIdentifier(imgName,"drawable",context.packageName)
            binding.dropMapImg.setImageResource(res)
            binding.root.setOnClickListener {
                mlistener?.OnHomeItemClick(it,adapterPosition)
            }
        }
    }

    override fun OnHomeItemClick(v: View, pos: Int) {
        mlistener?.OnHomeItemClick(v,pos)
    }

    fun setItemClickListener(listener: OnItemClickListener){
        mlistener=listener
    }
}