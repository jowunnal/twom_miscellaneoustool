package com.jinproject.twomillustratedbook.ui.screen.collection.setting.item

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
import com.jinproject.twomillustratedbook.databinding.CollectionSettingItemListItemBinding
import com.jinproject.twomillustratedbook.ui.listener.OnClickedListener
import com.jinproject.twomillustratedbook.ui.screen.collection.item.item.CollectionItemState
import dagger.hilt.android.qualifiers.ActivityContext
import java.text.DecimalFormat
import javax.inject.Inject

class CollectionSettingItemAdapter @Inject constructor(@ActivityContext private val context: Context):
    ListAdapter<CollectionItemState, CollectionSettingItemAdapter.ViewHolder>(diffUtil), OnClickedListener, Filterable {

    private val itemsUnfiltered: MutableList<CollectionItemState> = mutableListOf()
    private var onClickListener: OnClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            binding = CollectionSettingItemListItemBinding.inflate(LayoutInflater.from(parent.context)),
            onClick = onClickListener,
            context = context
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun setOnClickedListener(pos: Int) {
        onClickListener?.setOnClickedListener(pos)
    }

    fun setOnClickListener(listener: OnClickedListener) {
        this.onClickListener = listener
    }

    fun setItems(list: List<CollectionItemState>, onChanged: () -> Unit) {
        submitList(list, onChanged)

        val new = list.map { it.copy() }
        itemsUnfiltered.clear()
        itemsUnfiltered.addAll(new)
    }

    fun getItemInfo(pos: Int): CollectionItemState = getItem(pos)

    class ViewHolder(
        private val binding: CollectionSettingItemListItemBinding,
        private val onClick: OnClickedListener?,
        private val context: Context
    ): RecyclerView.ViewHolder(binding.root) {
        private var viewHolderLifecycleOwner: LifecycleOwner? = null

        init {
            itemView.doOnAttach {
                viewHolderLifecycleOwner = itemView.findViewTreeLifecycleOwner()
            }
            itemView.doOnDetach {
                viewHolderLifecycleOwner = null
            }
            binding.container.minWidth = context.applicationContext.resources.displayMetrics.widthPixels
            binding.container.setOnClickListener {
                onClick?.setOnClickedListener(adapterPosition)
            }
        }

        fun bind(item: CollectionItemState) {
            with(binding) {
                lifecycleOwner = viewHolderLifecycleOwner
                itemName = item.name
                price = DecimalFormat("#,###").format(item.price)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<CollectionItemState>() {
            override fun areItemsTheSame(
                oldItem: CollectionItemState,
                newItem: CollectionItemState
            ): Boolean =
                oldItem.name == newItem.name


            override fun areContentsTheSame(
                oldItem: CollectionItemState,
                newItem: CollectionItemState
            ): Boolean =
                oldItem == newItem

        }
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
                    val contentItemsFiltering = ArrayList<CollectionItemState>()

                    itemsUnfiltered.forEach { itemState ->
                        if(itemState.name.lowercase().contains(p0.toString().lowercase()))
                            contentItemsFiltering.add(itemState)
                    }

                    result.values = contentItemsFiltering
                }
                return result
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                val result = p1?.values as List<CollectionItemState>
                submitList(result)
            }
        }
    }
}