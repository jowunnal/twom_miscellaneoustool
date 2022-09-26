package com.jinproject.twomillustratedbook.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.Database.Entity.MonsDropItem
import com.jinproject.twomillustratedbook.Database.Entity.Monster
import com.jinproject.twomillustratedbook.databinding.DropItemBinding

class DropListAdapter(val context: Context) : RecyclerView.Adapter<DropListAdapter.ViewHolder>(),Filterable {
    var items=ArrayList<HashMap<Monster,List<MonsDropItem>>>()
    var items_unfiltered=ArrayList<HashMap<Monster,List<MonsDropItem>>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=DropItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(item :Map<Monster,List<MonsDropItem>>){
        val keys=item.keys
        for(key in keys){
            var map=HashMap<Monster,List<MonsDropItem>>()
            map.put(key,item.getValue(key))
            items.add(map)
        }
        items_unfiltered=items
    }

    inner class ViewHolder(private val binding: DropItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Map<Monster,List<MonsDropItem>>){
            var contents=""
            for(key in item.keys){
                for(value in item.getValue(key)){
                    contents+=value.mdItemName
                    if(item.getValue(key)[item.getValue(key).lastIndex]!=value){
                        contents+=", "
                    }
                }
            }
            binding.dropContent.text=contents
            binding.dropName.text=item.keys.elementAt(0).monsName
            binding.dropLevel.text=item.keys.elementAt(0).monsLevel.toString()
            binding.dropImg.setImageResource(context.resources.getIdentifier(item.keys.elementAt(0).monsImgName,"drawable",context.packageName))
        }
    }

    override fun getFilter(): Filter {
        return object :Filter(){
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if(p0.toString().isEmpty()){
                    items=items_unfiltered
                }
                else{
                    var items_filtering=ArrayList<HashMap<Monster,List<MonsDropItem>>>()
                    var filterflag=false
                    for(item in items_unfiltered){
                        for(key in item.keys){
                            if(key.monsName==p0.toString()){
                                filterflag=true
                            }
                            for(value in item.getValue(key)){
                                if(value.mdItemName.contains(p0.toString())){
                                    filterflag=true
                                }
                            }
                            if(filterflag){
                                var map=HashMap<Monster,List<MonsDropItem>>()
                                map.put(key,item.getValue(key))
                                items_filtering.add(map)
                                filterflag=false
                            }
                        }
                    }
                    items=items_filtering
                }
                val result=FilterResults()
                result.values=items
                return result
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                items=p1!!.values as ArrayList<HashMap<Monster,List<MonsDropItem>>>
                notifyDataSetChanged()
            }

        }
    }
}