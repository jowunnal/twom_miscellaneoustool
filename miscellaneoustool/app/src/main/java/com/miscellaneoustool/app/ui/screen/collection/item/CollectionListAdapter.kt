package com.miscellaneoustool.app.ui.screen.collection.item

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
import com.miscellaneoustool.app.databinding.CollectionListItemBinding
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionState
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CollectionListAdapter @Inject constructor(@ActivityContext private val context: Context) :
    RecyclerView.Adapter<CollectionListAdapter.BookViewHolder>(), Filterable {
    private var items = ArrayList<CollectionState>()
    private var itemsUnfiltered = ArrayList<CollectionState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = CollectionListItemBinding.inflate(LayoutInflater.from(parent.context))
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(items[position])
    }


    override fun getItemCount(): Int {
        return this.items.size
    }


    fun setItems(items: List<CollectionState>) {
        this.items.clear()
        this.items.addAll(items)

        itemsUnfiltered = this.items
    }


    inner class BookViewHolder(private val binding: CollectionListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var lifecycleOwner: LifecycleOwner? = null

        init {
            binding.containerInBook.minWidth =
                context.applicationContext.resources.displayMetrics.widthPixels
            itemView.doOnAttach {
                lifecycleOwner = itemView.findViewTreeLifecycleOwner()
            }
            itemView.doOnDetach {
                lifecycleOwner = null
            }
        }

        fun bind(item: CollectionState) {
            binding.lifecycleOwner = lifecycleOwner
            binding.itemName = item.items.joinToString("\n") { itemState ->
                StringBuilder().apply {
                    if (itemState.enchantNumber != 0)
                        append("+${itemState.enchantNumber} ")

                    append(itemState.name)

                    if (itemState.count != 1)
                        append(" * ${itemState.count}")
                }.toString()
            }

            binding.stat = item.stats.joinToString("\n") { statState ->
                StringBuilder().append(
                    isValidItemStat(
                        stat = statState.value,
                        statName = statState.name,
                        isPercentValue = statState.name.contains("%")
                    )
                ).toString()
            }
        }

        private fun isValidItemStat(
            stat: Double,
            statName: String,
            isPercentValue: Boolean
        ): String =
            if (isPercentValue)
                "${statName.replace("%", "")}: $stat%"
            else
                "$statName: $stat"
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                if (p0.toString().isEmpty()) {
                    items = itemsUnfiltered
                } else {
                    val contentItemsFiltering = ArrayList<CollectionState>()

                    itemsUnfiltered.forEach { collectionState ->
                        collectionState.stats.forEach { statState ->
                            if (statState.name.contains(p0.toString()) && statState.value != 0.0)
                                contentItemsFiltering.add(collectionState)
                        }
                        collectionState.items.forEach { itemState ->
                            if (itemState.name.contains(p0.toString()))
                                contentItemsFiltering.add(collectionState)
                        }
                    }

                    items = contentItemsFiltering
                }
                return FilterResults().apply { values = items }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                items = p1!!.values as ArrayList<CollectionState>
                notifyDataSetChanged()
            }
        }
    }
}