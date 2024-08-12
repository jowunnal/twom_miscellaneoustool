package com.jinproject.features.collection.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.features.collection.databinding.CollectionCategoryItemBinding
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CollectionCategoryAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<CollectionCategoryAdapter.ViewHolder>(),
    com.jinproject.features.core.listener.OnClickedListener {

    private var items = ArrayList<com.jinproject.domain.model.Category>()
    private var mlistener: com.jinproject.features.core.listener.OnClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CollectionCategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<com.jinproject.domain.model.Category>) {
        this.items.clear()
        this.items.addAll(items)
    }

    fun getItem(pos: Int): String {
        return items[pos].storedName
    }

    inner class ViewHolder(private val binding: CollectionCategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var lifecycleOwner: LifecycleOwner? = null

        init {
            binding.collectionCategoryItem.minimumWidth =
                context.applicationContext.resources.displayMetrics.widthPixels
            binding.collectionCategoryImg.minimumWidth =
                context.applicationContext.resources.displayMetrics.widthPixels
            itemView.doOnAttach {
                lifecycleOwner = itemView.findViewTreeLifecycleOwner()
            }
            itemView.doOnDetach {
                lifecycleOwner = null
            }
            binding.root.setOnClickListener {
                setOnClickedListener(adapterPosition)
            }
        }

        fun bind(item: com.jinproject.domain.model.Category) {
            binding.lifecycleOwner = lifecycleOwner
            binding.activityContext = context
            binding.category = item
        }
    }

    fun setItemClickListener(listener: com.jinproject.features.core.listener.OnClickedListener) {
        mlistener = listener
    }

    override fun setOnClickedListener(pos: Int) {
        mlistener?.setOnClickedListener(pos)
    }
}