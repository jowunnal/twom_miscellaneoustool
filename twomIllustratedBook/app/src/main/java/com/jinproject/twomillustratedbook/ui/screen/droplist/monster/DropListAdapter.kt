package com.jinproject.twomillustratedbook.ui.screen.droplist.monster

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.databinding.DropItemBinding
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.ItemState
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.MonsterState
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DropListAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<DropListAdapter.ViewHolder>(), Filterable {

    var items = ArrayList<MonsterState>()
    var itemsUnfiltered = ArrayList<MonsterState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DropItemBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(item: List<MonsterState>) {
        this.items.clear()
        this.items.addAll(item)
        itemsUnfiltered = items
    }

    inner class ViewHolder(private val binding: DropItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("DiscouragedApi")
        fun bind(monster: MonsterState) {

            binding.dropContent.text = itemListToSingleString(monster.item)
            binding.dropName.text = monster.name
            binding.dropLevel.text = monster.level.toString()
            binding.dropImg.setImageResource(
                context.resources.getIdentifier(
                    monster.imgName, "drawable", context.packageName
                )
            )
        }

        private fun itemListToSingleString(itemList: List<ItemState>): String {
            var contents = ""
            itemList.forEachIndexed { index, item ->
                contents += if (itemList.lastIndex != index) "${item.name}, " else item.name
            }
            return contents
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                items = if (p0.isNullOrBlank() || p0.isEmpty()) {
                    itemsUnfiltered
                } else {
                    val itemsFiltering = ArrayList<MonsterState>()
                    itemsUnfiltered.forEach { monster ->
                        if (monster.name == p0.toString() || monster.item.contains(ItemState(name = p0.toString(), count = 0)))
                            itemsFiltering.add(monster)
                    }
                    itemsFiltering
                }
                return FilterResults().apply { values = items }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                items = p1!!.values as ArrayList<MonsterState>
                notifyDataSetChanged()
            }

        }
    }
}