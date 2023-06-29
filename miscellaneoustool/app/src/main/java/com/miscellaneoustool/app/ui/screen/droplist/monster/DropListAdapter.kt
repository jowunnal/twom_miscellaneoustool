package com.miscellaneoustool.app.ui.screen.droplist.monster

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.miscellaneoustool.app.databinding.DropItemBinding
import com.miscellaneoustool.app.ui.screen.droplist.monster.item.ItemState
import com.miscellaneoustool.app.ui.screen.droplist.monster.item.MonsterState

class DropListAdapter(
    private val context: Context,
    private val getMonsterItem: (List<ItemState>) -> String
) :
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

    fun setItems(items: List<MonsterState>) {
        this.items.clear()
        this.items.addAll(items)

        itemsUnfiltered = this.items
    }

    inner class ViewHolder(private val binding: DropItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var lifecycleOwner: LifecycleOwner? = null

        init {
            binding.dropItem.minWidth =
                context.applicationContext.resources.displayMetrics.widthPixels
            itemView.doOnAttach {
                lifecycleOwner = itemView.findViewTreeLifecycleOwner()
            }
            itemView.doOnDetach {
                lifecycleOwner = null
            }
        }

        fun bind(monster: MonsterState) {
            binding.lifecycleOwner = lifecycleOwner
            binding.activityContext = context
            binding.monster = monster

            binding.dropContent.text = getMonsterItem(monster.item ?: emptyList())
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
                        if (monster.name.contains(p0.toString()))
                            itemsFiltering.add(monster)
                        monster.item?.forEach { itemState ->
                            if (itemState.name.contains(p0.toString()))
                                itemsFiltering.add(monster)
                        }
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