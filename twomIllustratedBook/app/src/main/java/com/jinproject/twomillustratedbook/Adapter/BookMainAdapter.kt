package com.jinproject.twomillustratedbook.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.Database.Entity.BookContentEntity
import com.jinproject.twomillustratedbook.Database.Entity.BookEntity
import com.jinproject.twomillustratedbook.databinding.BookItemBinding

class BookMainAdapter : RecyclerView.Adapter<BookMainAdapter.BookViewHolder>() ,Filterable{
    var contentItems = ArrayList<HashMap<BookEntity,List<BookContentEntity>>>()
    var contentItemsUnfiltered= ArrayList<HashMap<BookEntity,List<BookContentEntity>>>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = BookItemBinding.inflate(LayoutInflater.from(parent.context))
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(contentItems[position])
    }


    override fun getItemCount(): Int {
        return contentItems.size
    }


    fun setContentItem(items: Map<BookEntity,List<BookContentEntity>>){ // key에대한 value들을 쪼개서 arrayList에 담음
        this.contentItems.clear()
        val keys=items.keys
        for(key in keys){ // key= BookEntity의 id값
            val map=HashMap<BookEntity,List<BookContentEntity>>() // 한덩어리로쪼개어 넣을 HashMap 할당
            val value=items.getValue(key) // 하나의 key에대한 List값을 뽑아서
            map.put(key,value) //null이 아니면 집어넣음
            this.contentItems.add(map) // 하나의 key에 대한 value값들 을 arraylist에 넣음
        }
        contentItemsUnfiltered=contentItems
    }


    inner class BookViewHolder(private val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item1:HashMap<BookEntity,List<BookContentEntity>>){
            val keys=item1.keys
            var str=""
            for(key in keys){
                val value=item1.getValue(key)
                for(v in value){ // key에대한 value값이 다중값이기때문에 각값을 뽑아 \n을 사용하여 구분하여 tv에 넣음
                    str+=v.material + if(v.count==1){""} else {" * "+v.count}
                    if(value[value.lastIndex]!=v){
                        str+="\n"
                    }
                }

            }

            binding.tvContent.text=str
            var strStat=""
            var flagCnt=false

            val v =keys.elementAt(0)

            if(v.hp!=0.0){ strStat+="체력: "+v.hp.toString()
                flagCnt=true}
            if(v.mp!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="마나: "+v.mp.toString()
                flagCnt=true }

            if(v.hp_per!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="체력: "+v.hp_per.toString()+"%"
                flagCnt=true}

            if(v.mp_per!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="마나: "+v.mp_per.toString()+"%"
                flagCnt=true}

            if(v.hp_regen!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="체력재생: "+v.hp_regen.toString()
                flagCnt=true}

            if(v.mp_regen!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="마나재생: "+v.mp_regen.toString()
                flagCnt=true}

            if(v.hr!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="명중: "+v.hr.toString()
                flagCnt=true}

            if(v.cri!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="크리티컬: "+v.cri.toString()
                flagCnt=true}

            if(v.stat_int!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="지능: "+v.stat_int.toString()
                flagCnt=true}

            if(v.stat_str!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="힘: "+v.stat_str.toString()
                flagCnt=true}

            if(v.stat_dex!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="민첩: "+v.stat_dex.toString()
                flagCnt=true}

            if(v.move!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="이동속도: "+v.move.toString()
                flagCnt=true}

            if(v.armor!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="방어력: "+v.armor.toString()
                flagCnt=true}

            if(v.pve_dmg!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지: "+v.pve_dmg.toString()
                flagCnt=true}

            if(v.pvp_dmg!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지: "+v.pvp_dmg.toString()
                flagCnt=true}

            if(v.pve_dmg_per!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지: "+v.pve_dmg_per.toString()+"%"
                flagCnt=true}

            if(v.pvp_dmg_per!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지: "+v.pvp_dmg_per.toString()+"%"
                flagCnt=true}

            if(v.pve_dmg_down!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지감소: "+v.pve_dmg_down.toString()
                flagCnt=true}

            if(v.pvp_dmg_down!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지감소: "+v.pvp_dmg_down.toString()
                flagCnt=true}

            if(v.pve_dmg_down_per!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지감소: "+v.pve_dmg_down_per.toString()+"%"
                flagCnt=true}

            if(v.pvp_dmg_down_per!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지감소: "+v.pvp_dmg_down_per.toString()+"%"
                flagCnt=true}

            if(v.item_drop!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="아이템드랍률: "+v.item_drop.toString()
                flagCnt=true}

            if(v.gold_drop!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="골드드랍률: "+v.gold_drop.toString() }

            binding.tvStat.text=strStat
        }

    }

    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val inputData=p0.toString()
                if(inputData.isEmpty()){
                    contentItems=contentItemsUnfiltered
                }
                else{
                    var contentItemsFiltering = ArrayList<HashMap<BookEntity,List<BookContentEntity>>>()
                    var filterFlag=false
                    var filterStatFlag=false
                    for(items in contentItemsUnfiltered){
                        val keys=items.keys
                        for(key in keys){
                            val values=items.getValue(key)
                            filterStatFlag=searchStatFilter(inputData,key)

                            for(value in values){
                                if(value.material.contains(inputData)){
                                    filterFlag=true
                                }
                            }
                            if(filterFlag || filterStatFlag){
                                val map=HashMap<BookEntity,List<BookContentEntity>>()
                                map.put(key,values)
                                contentItemsFiltering.add(map)
                                filterFlag=false
                                filterStatFlag=false
                            }
                        }
                    }
                    contentItems=contentItemsFiltering
                }
                val result=FilterResults()
                result.values=contentItems
                return result
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                contentItems= p1!!.values as ArrayList<HashMap<BookEntity, List<BookContentEntity>>>
                notifyDataSetChanged()
            }
        }
    }
    fun searchStatFilter(inputData:String,stat:BookEntity):Boolean{
        when(inputData){
            "체력"->{if(stat.hp!=0.0){ return true }
                    else if(stat.hp_per!=0.0){ return true }
            }
            "마나"->{if(stat.mp!=0.0){ return true }
                     else if(stat.mp_per!=0.0){ return true }
            }
            "체력재생"->if(stat.hp_regen!=0.0){ return true }
            "마나재생"-> if(stat.mp_regen!=0.0){ return true }
            "명중"->if(stat.hr!=0.0){ return true }
            "크리티컬"->if(stat.cri!=0.0){ return true }
            "지능"->if(stat.stat_int!=0.0){ return true }
            "힘"->if(stat.stat_str!=0.0){ return true }
            "민첩"->if(stat.stat_dex!=0.0){ return true }
            "이동속도"->if(stat.move!=0.0){ return true }
            "방어력"->if(stat.armor!=0.0){ return true }
            "pve데미지"->{if(stat.pve_dmg!=0.0){ return true }
                        else if(stat.pve_dmg_per!=0.0){ return true }
            }
            "pvp데미지"->{if(stat.pvp_dmg!=0.0){ return true }
                        else if(stat.pvp_dmg_per!=0.0){ return true }
            }
            "pve데미지감소"->{if(stat.pve_dmg_down!=0.0){ return true }
                             else if(stat.pve_dmg_down_per!=0.0){ return true }
            }
            "pvp데미지감소"->{if(stat.pvp_dmg_down!=0.0){ return true }
                           else if(stat.pvp_dmg_down_per!=0.0){ return true }
            }
            "아이템드랍률"->if(stat.item_drop!=0.0){ return true }
            "골드드랍률"->if(stat.gold_drop!=0.0){ return true }
        }
        return false
    }

}