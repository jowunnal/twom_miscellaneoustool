package com.jinproject.features.core.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.jinproject.domain.model.ItemType

@SuppressLint("DiscouragedApi")
@BindingAdapter("context", "imgName", "type")
fun setImageResource(imgView: ImageView, context: Context, imgName: String, type: Boolean) {
    Glide.with(context)
        .load(Uri.parse("file:///android_asset/img/${if(type) "category" else "monster"}/$imgName.png"))
        .into(imgView)
}

@BindingAdapter("context","category")
fun setCategoryOnLocale(textView: TextView, context: Context, itemType: ItemType) {
    /*textView.text = context.doOnLocaleLanguage(
        onKo = itemType.displayName,
        onElse = itemType.storedName
    )*/
}