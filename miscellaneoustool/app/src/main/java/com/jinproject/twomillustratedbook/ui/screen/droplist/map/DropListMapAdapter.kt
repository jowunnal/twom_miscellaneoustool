package com.jinproject.twomillustratedbook.ui.screen.droplist.map

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jinproject.twomillustratedbook.databinding.DroplistmapItemBinding
import com.jinproject.twomillustratedbook.ui.listener.OnClickedListener
import com.jinproject.twomillustratedbook.ui.screen.droplist.map.item.MapState
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class DropListMapAdapter @Inject constructor(@ActivityContext val context: Context) :
    RecyclerView.Adapter<DropListMapAdapter.ViewHolder>(), OnClickedListener {
    var items = ArrayList<MapState>()
    var mListener: OnClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(DroplistmapItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(item: List<MapState>) {
        this.items.clear()
        this.items.addAll(item)
    }

    fun getItem(pos: Int): String {
        return items[pos].name
    }

    inner class ViewHolder(private val binding: DroplistmapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.dropListMapItem.minimumWidth =
                context.applicationContext.resources.displayMetrics.widthPixels
            binding.root.setOnClickListener {
                setOnClickedListener(adapterPosition)
            }
        }

        @SuppressLint("DiscouragedApi")
        fun bind(item: MapState) {
            binding.dropMapName.text = item.name

            Glide.with(itemView)
                .load(Uri.parse("file:///android_asset/img/monster/${item.imgName}.png"))
                .into(binding.dropMapImg)
        }
    }

    override fun setOnClickedListener(pos: Int) {
        mListener?.setOnClickedListener(pos)
    }

    fun setItemClickListener(listener: OnClickedListener) {
        mListener = listener
    }
}