package com.jinproject.twomillustratedbook.ui.screen.collection.item

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
import com.jinproject.twomillustratedbook.data.database.Entity.Book
import com.jinproject.twomillustratedbook.data.database.Entity.RegisterItemToBook
import com.jinproject.twomillustratedbook.databinding.CollectionListItemBinding
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionState
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

            var itemInfo = ""
            item.items.forEachIndexed { index, itemState ->
                if (itemState.enchantNumber != 0)
                    itemInfo += "+${itemState.enchantNumber} "

                if (itemState.count != 1)
                    itemInfo += "${itemState.name} * ${itemState.count}"
                else
                    itemInfo += itemState.name

                if (index != item.items.lastIndex)
                    itemInfo += "\n"
            }

            binding.tvContent.text = itemInfo

            var statInfo = ""
            item.stats.forEach { statState ->
                statInfo += isValidItemStat(
                    stat = statState.value,
                    statName = statState.name,
                    isPercentValue = statState.name.contains("퍼센트")
                )
            }
            binding.tvStat.text = statInfo
        }

        private fun isValidItemStat(
            stat: Double,
            statName: String,
            isPercentValue: Boolean
        ): String =
            if (stat != 0.0) {
                if (isPercentValue)
                    "${statName.replace("퍼센트","")}: $stat%\n"
                else
                    "$statName: $stat\n"
            } else
                ""

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