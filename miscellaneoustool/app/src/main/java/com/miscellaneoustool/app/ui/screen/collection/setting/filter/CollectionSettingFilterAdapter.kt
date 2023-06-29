package com.miscellaneoustool.app.ui.screen.collection.setting.filter

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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miscellaneoustool.app.databinding.CollectionListItemBinding
import com.miscellaneoustool.app.ui.listener.OnClickedListener
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionState
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CollectionSettingFilterAdapter @Inject constructor(@ActivityContext private val context: Context) :
    ListAdapter<CollectionState, CollectionSettingFilterAdapter.BookViewHolder>(diffUtil), Filterable, OnClickedListener {

    private var itemsUnfiltered: MutableList<CollectionState> = mutableListOf()
    private var onClickedListener: OnClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = CollectionListItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        return BookViewHolder(
            binding = binding,
            context = context,
            onClicked = onClickedListener
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun setOnClickedListener(pos: Int) {
        onClickedListener?.setOnClickedListener(pos)
    }

    fun setItems(items: List<CollectionState>) {
        submitList(items)

        val old = items.toMutableList()
        val new = old.map { it.copy() }
        itemsUnfiltered.clear()
        itemsUnfiltered.addAll(new)
    }

    fun getItemIdOnPosition(pos: Int): CollectionState = getItem(pos)

    fun setOnClickListener(listener: OnClickedListener) {
        this.onClickedListener = listener
    }

    class BookViewHolder(
        private val binding: CollectionListItemBinding,
        private val context: Context,
        private val onClicked: OnClickedListener?
    ) : RecyclerView.ViewHolder(binding.root) {

        private var viewHolderLifecycleOwner: LifecycleOwner? = null

        init {
            with(binding) {
                containerInBook.minWidth =
                    context.applicationContext.resources.displayMetrics.widthPixels

                containerInBook.setOnClickListener {
                    onClicked?.setOnClickedListener(adapterPosition)
                }
            }
            itemView.doOnAttach {
                viewHolderLifecycleOwner = itemView.findViewTreeLifecycleOwner()
            }
            itemView.doOnDetach {
                viewHolderLifecycleOwner = null
            }
        }

        fun bind(item: CollectionState) {
            with(binding) {
                lifecycleOwner = viewHolderLifecycleOwner
                itemName = item.items.joinToString("\n") { itemState ->
                    StringBuilder().apply {
                        if (itemState.enchantNumber != 0)
                            append("+${itemState.enchantNumber} ")

                        append(itemState.name)

                        if (itemState.count != 1)
                            append(" * ${itemState.count}")
                    }.toString()
                }
                stat = item.stats.joinToString("\n") { statState ->
                    StringBuilder().append(
                        isValidItemStat(
                            stat = statState.value,
                            statName = statState.name,
                            isPercentValue = statState.name.contains("%")
                        )
                    ).toString()
                }
                price = item.items.fold(0) { acc, collectionItemState ->
                    acc + collectionItemState.count * collectionItemState.price
                }.toString()
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
                val result = FilterResults()

                if (p0.toString().isEmpty()) {
                    val old = itemsUnfiltered.toMutableList()
                    val new = old.map { it.copy() }
                    result.values = new
                } else {
                    val contentItemsFiltering = ArrayList<CollectionState>()

                    itemsUnfiltered.forEach { collectionState ->
                        collectionState.stats.forEach { statState ->
                            if (statState.name.contains(p0.toString()))
                                contentItemsFiltering.add(collectionState)
                        }
                        collectionState.items.forEach { itemState ->
                            if (itemState.name.contains(p0.toString()))
                                contentItemsFiltering.add(collectionState)
                        }
                    }

                    result.values = contentItemsFiltering
                }
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                val result = p1?.values as List<CollectionState>
                submitList(result)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CollectionState>() {
            override fun areItemsTheSame(
                oldItem: CollectionState,
                newItem: CollectionState
            ): Boolean =
                if (oldItem.isCheck != newItem.isCheck) {
                    false
                } else {
                    oldItem.id == newItem.id
                }

            override fun areContentsTheSame(
                oldItem: CollectionState,
                newItem: CollectionState
            ): Boolean =
                oldItem == newItem

        }
    }
}