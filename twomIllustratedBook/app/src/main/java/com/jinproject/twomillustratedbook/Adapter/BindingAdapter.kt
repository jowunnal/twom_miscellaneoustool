package com.jinproject.twomillustratedbook.Adapter

import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("android:onTextChanged")
fun onTextChanged(view: TextInputEditText, inputString: (String?)->Unit){
    view.doOnTextChanged { text, start, before, count -> inputString(text.toString()) }
}