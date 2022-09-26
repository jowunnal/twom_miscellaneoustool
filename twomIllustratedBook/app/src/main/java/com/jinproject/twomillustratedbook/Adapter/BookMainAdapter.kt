package com.jinproject.twomillustratedbook.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.Database.Entity.Book
import com.jinproject.twomillustratedbook.Database.Entity.RegisterItemToBook
import com.jinproject.twomillustratedbook.databinding.BookItemBinding

class BookMainAdapter : RecyclerView.Adapter<BookMainAdapter.BookViewHolder>() ,Filterable{
    var contentItems = ArrayList<HashMap<Book,List<RegisterItemToBook>>>()
    var contentItemsUnfiltered= ArrayList<HashMap<Book,List<RegisterItemToBook>>>()


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


    fun setContentItem(items: Map<Book,List<RegisterItemToBook>>){ // key에대한 value들을 쪼개서 arrayList에 담음
        this.contentItems.clear()
        val keys=items.keys
        for(key in keys){ // key= BookEntity의 id값
            val map=HashMap<Book,List<RegisterItemToBook>>() // 한덩어리로쪼개어 넣을 HashMap 할당
            val value=items.getValue(key) // 하나의 key에대한 List값을 뽑아서
            map.put(key,value) //null이 아니면 집어넣음
            this.contentItems.add(map) // 하나의 key에 대한 value값들 을 arraylist에 넣음
        }
        contentItemsUnfiltered=contentItems
    }


    inner class BookViewHolder(private val binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item1:HashMap<Book,List<RegisterItemToBook>>){
            val keys=item1.keys
            var str=""
            for(key in keys){
                val value=item1.getValue(key)
                for(v in value){ // key에대한 value값이 다중값이기때문에 각값을 뽑아 \n을 사용하여 구분하여 tv에 넣음
                    str+=v.rlItemName + if(v.rlItemCount==1){""} else {" * "+v.rlItemCount}
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

            if(v.hpPer!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="체력: "+v.hpPer.toString()+"%"
                flagCnt=true}

            if(v.mpPer!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="마나: "+v.mpPer.toString()+"%"
                flagCnt=true}

            if(v.hpRegen!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="체력재생: "+v.hpRegen.toString()
                flagCnt=true}

            if(v.mpRegen!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="마나재생: "+v.mpRegen.toString()
                flagCnt=true}

            if(v.hr!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="명중: "+v.hr.toString()
                flagCnt=true}

            if(v.cri!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="크리티컬: "+v.cri.toString()
                flagCnt=true}

            if(v.statInt!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="지능: "+v.statInt.toString()
                flagCnt=true}

            if(v.statStr!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="힘: "+v.statStr.toString()
                flagCnt=true}

            if(v.statDex!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="민첩: "+v.statDex.toString()
                flagCnt=true}

            if(v.move!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="이동속도: "+v.move.toString()
                flagCnt=true}

            if(v.armor!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="방어력: "+v.armor.toString()
                flagCnt=true}

            if(v.pveDmg!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지: "+v.pveDmg.toString()
                flagCnt=true}

            if(v.pvpDmg!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지: "+v.pvpDmg.toString()
                flagCnt=true}

            if(v.pveDmgPer!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지: "+v.pveDmgPer.toString()+"%"
                flagCnt=true}

            if(v.pvpDmgPer!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지: "+v.pvpDmgPer.toString()+"%"
                flagCnt=true}

            if(v.pveDmgDown!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지감소: "+v.pveDmgDown.toString()
                flagCnt=true}

            if(v.pvpDmgDown!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지감소: "+v.pvpDmgDown.toString()
                flagCnt=true}

            if(v.pveDmgDownPer!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pve데미지감소: "+v.pveDmgDownPer.toString()+"%"
                flagCnt=true}

            if(v.pvpDmgDownPer!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="pvp데미지감소: "+v.pvpDmgDownPer.toString()+"%"
                flagCnt=true}

            if(v.itemDrop!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="아이템드랍률: "+v.itemDrop.toString()
                flagCnt=true}

            if(v.goldDrop!=0.0){
                if(flagCnt){ strStat+="\n" }
                strStat+="골드드랍률: "+v.goldDrop.toString() }

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
                    var contentItemsFiltering = ArrayList<HashMap<Book,List<RegisterItemToBook>>>()
                    var filterFlag=false
                    var filterStatFlag=false
                    for(items in contentItemsUnfiltered){
                        val keys=items.keys
                        for(key in keys){
                            val values=items.getValue(key)
                            filterStatFlag=searchStatFilter(inputData,key)

                            for(value in values){
                                if(value.rlItemName.contains(inputData)){
                                    filterFlag=true
                                }
                            }
                            if(filterFlag || filterStatFlag){
                                val map=HashMap<Book,List<RegisterItemToBook>>()
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
                contentItems= p1!!.values as ArrayList<HashMap<Book, List<RegisterItemToBook>>>
                notifyDataSetChanged()
            }
        }
    }
    fun searchStatFilter(inputData:String,stat:Book):Boolean{
        when(inputData){
            "체력"->{if(stat.hp!=0.0){ return true }
                    else if(stat.hpPer!=0.0){ return true }
            }
            "마나"->{if(stat.mp!=0.0){ return true }
                     else if(stat.mpPer!=0.0){ return true }
            }
            "체력재생"->if(stat.hpRegen!=0.0){ return true }
            "마나재생"-> if(stat.mpRegen!=0.0){ return true }
            "명중"->if(stat.hr!=0.0){ return true }
            "크리티컬"->if(stat.cri!=0.0){ return true }
            "지능"->if(stat.statInt!=0.0){ return true }
            "힘"->if(stat.statStr!=0.0){ return true }
            "민첩"->if(stat.statDex!=0.0){ return true }
            "이동속도"->if(stat.move!=0.0){ return true }
            "방어력"->if(stat.armor!=0.0){ return true }
            "pve데미지"->{if(stat.pveDmg!=0.0){ return true }
                        else if(stat.pveDmgPer!=0.0){ return true }
            }
            "pvp데미지"->{if(stat.pvpDmg!=0.0){ return true }
                        else if(stat.pvpDmgPer!=0.0){ return true }
            }
            "pve데미지감소"->{if(stat.pveDmgDown!=0.0){ return true }
                             else if(stat.pveDmgDownPer!=0.0){ return true }
            }
            "pvp데미지감소"->{if(stat.pvpDmgDown!=0.0){ return true }
                           else if(stat.pvpDmgDownPer!=0.0){ return true }
            }
            "아이템드랍률"->if(stat.itemDrop!=0.0){ return true }
            "골드드랍률"->if(stat.goldDrop!=0.0){ return true }
        }
        return false
    }

}