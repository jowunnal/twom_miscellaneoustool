package com.jinproject.twomillustratedbook.ui.screen.collection.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.core.view.doOnDetach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.jinproject.twomillustratedbook.databinding.CollectionCategoryItemBinding
import com.jinproject.twomillustratedbook.domain.model.Category
import com.jinproject.twomillustratedbook.ui.listener.OnItemClickListener
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class CollectionCategoryAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<CollectionCategoryAdapter.ViewHolder>(), OnItemClickListener {

    private var items = ArrayList<Category>()
    private var mlistener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CollectionCategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItem(items: List<Category>) {
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
                OnHomeItemClick(it, adapterPosition)
            }
        }

        fun bind(item: Category) {
            binding.lifecycleOwner = lifecycleOwner
            binding.activityContext = context
            binding.category = item
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mlistener = listener
    }

    override fun OnHomeItemClick(v: View, pos: Int) {
        mlistener?.OnHomeItemClick(v, pos)
    }
}