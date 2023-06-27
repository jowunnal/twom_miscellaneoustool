package com.miscellaneoustool.app.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("android:onTextChanged")
fun onTextChanged(view: TextInputEditText, inputString: (String?) -> Unit) {
    view.doOnTextChanged { text, start, before, count -> inputString(text.toString()) }
}

@BindingAdapter("selectedItem")
fun selectedItem(view: Spinner, bossItem: (String) -> Unit) {
    view.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            bossItem(p0?.selectedItem.toString())
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
}

@SuppressLint("DiscouragedApi")
@BindingAdapter("context", "imgName")
fun setImageResource(imgView: ImageView, context: Context, imgName: String) {
    val res = context.resources.getIdentifier(imgName, "drawable", context.packageName)
    imgView.setImageResource(res)
}