package com.miscellaneoustool.app.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@SuppressLint("DiscouragedApi")
@BindingAdapter("context", "imgName", "type")
fun setImageResource(imgView: ImageView, context: Context, imgName: String, type: Boolean) {
    Glide.with(context)
        .load(Uri.parse("file:///android_asset/img/${if(type) "category" else "monster"}/$imgName.png"))
        .into(imgView)
}