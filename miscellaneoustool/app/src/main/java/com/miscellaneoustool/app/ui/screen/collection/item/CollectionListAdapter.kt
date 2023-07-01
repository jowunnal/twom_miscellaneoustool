package com.miscellaneoustool.app.ui.screen.collection.item

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
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
import com.miscellaneoustool.app.R
import com.miscellaneoustool.app.databinding.CollectionListItemBinding
import com.miscellaneoustool.app.ui.listener.OnClickedListener
import com.miscellaneoustool.app.ui.listener.OnLongClickedListener
import com.miscellaneoustool.app.ui.screen.collection.item.item.CollectionState
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CollectionListAdapter @Inject constructor(@ActivityContext private val context: Context) :
    ListAdapter<CollectionState, CollectionListAdapter.BookViewHolder>(diffUtil), Filterable, OnClickedListener, OnLongClickedListener {

    private var itemsUnfiltered: MutableList<CollectionState> = mutableListOf()
    private var onClickedListener: OnClickedListener? = null
    private var onLongClickedListener: OnLongClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = CollectionListItemBinding.inflate(LayoutInflater.from(parent.context))
        return BookViewHolder(
            binding = binding,
            context = context,
            onClicked = onClickedListener,
            onLongClicked = onLongClickedListener
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun setOnClickedListener(pos: Int) {
        onClickedListener?.setOnClickedListener(pos)
    }

    override fun setOnLongClickedListener(pos: Int) {
        onLongClickedListener?.setOnLongClickedListener(pos)
    }

    fun setItems(items: List<CollectionState>, onChanged: () -> Unit = {}) {
        submitList(items) {
            onChanged()
        }

        val old = items.toMutableList()
        val new = old.map { it.copy() }
        itemsUnfiltered.clear()
        itemsUnfiltered.addAll(new)
    }

    fun getCollectionItem(pos: Int): CollectionState = getItem(pos)

    fun setItemsEnableClick(onSubmitCallback: () -> Unit) {
        val old = currentList.toList()
        val new = old.map { it.copy(isCheck = CollectionState.CheckState.UNCHECKED) }
        submitList(new) {
            onSubmitCallback()
        }
    }

    fun setItemsDisableClick(onSubmitCallback: () -> Unit) {
        val old = currentList.toList()
        val new = old.map { it.copy(isCheck = CollectionState.CheckState.INVISIBLE) }
        submitList(new) {
            onSubmitCallback()
        }
    }

    fun setItemCheck(pos: Int) {
        val state = when(getItem(pos).isCheck) {
            CollectionState.CheckState.CHECKED -> CollectionState.CheckState.UNCHECKED
            CollectionState.CheckState.UNCHECKED -> CollectionState.CheckState.CHECKED
            CollectionState.CheckState.INVISIBLE -> CollectionState.CheckState.INVISIBLE
        }

        val old = currentList.toMutableList().apply {
            set(pos, getItem(pos).copy(isCheck = state))
        }

        submitList(old)
    }

    fun getItemCheck() = kotlin.runCatching { currentList.first().isCheck != CollectionState.CheckState.INVISIBLE }.getOrDefault(true)

    fun getCheckedItems(): List<Int> =
        currentList
            .filter { collection -> collection.isCheck == CollectionState.CheckState.CHECKED }
            .map { collection -> collection.id }

    fun setOnClickListener(listener: OnClickedListener) {
        this.onClickedListener = listener
    }

    fun setOnLongClickListener(listener: OnLongClickedListener) {
        this.onLongClickedListener = listener
    }

    class BookViewHolder(
        private val binding: CollectionListItemBinding,
        private val context: Context,
        private val onClicked: OnClickedListener?,
        private val onLongClicked: OnLongClickedListener?
    ): RecyclerView.ViewHolder(binding.root) {

        private var viewHolderLifecycleOwner: LifecycleOwner? = null

        init {
            with(binding) {
                containerInBook.apply {
                    minWidth = context.applicationContext.resources.displayMetrics.widthPixels
                    setPadding(16,0,16,0)
                    setOnLongClickListener {
                        onLongClicked?.setOnLongClickedListener(adapterPosition)
                        true
                    }
                    setOnClickListener {
                        onClicked?.setOnClickedListener(adapterPosition)
                    }
                }
            }
            with(itemView) {
                doOnAttach {
                    viewHolderLifecycleOwner = itemView.findViewTreeLifecycleOwner()
                }
                doOnDetach {
                    viewHolderLifecycleOwner = null
                }
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

            when(item.isCheck) {
                CollectionState.CheckState.UNCHECKED -> {
                    with(binding) {
                        imgCheck.visibility = View.VISIBLE
                        imgCheck.setImageResource(R.drawable.ic_check)
                        tvContent.alpha = 1f
                        tvStat.alpha = 1f
                        tvPrice.alpha = 1f
                    }
                }
                CollectionState.CheckState.CHECKED -> {
                    with(binding) {
                        imgCheck.visibility = View.VISIBLE
                        imgCheck.setImageResource(R.drawable.ic_check_clicked)
                        tvContent.alpha = 0.3f
                        tvStat.alpha = 0.3f
                        tvPrice.alpha = 0.3f
                    }
                }
                CollectionState.CheckState.INVISIBLE -> {
                    with(binding) {
                        imgCheck.visibility = View.INVISIBLE
                        tvContent.alpha = 1f
                        tvStat.alpha = 1f
                        tvPrice.alpha = 1f
                    }
                }
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
                    val new = old.map{ it.copy() }
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
        val diffUtil = object: DiffUtil.ItemCallback<CollectionState>() {
            override fun areItemsTheSame(
                oldItem: CollectionState,
                newItem: CollectionState
            ): Boolean =
                if(oldItem.isCheck != newItem.isCheck){
                    false
                }
                else{
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