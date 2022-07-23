package com.jinproject.twomillustratedbook.listener

import android.view.View
import com.jinproject.twomillustratedbook.Item.CheckStatue
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding

interface OnBossNameClickedListener {
    fun setOnItemClickListener(v:View,pos:Int,binding:AlarmUserSelectedItemBinding)
}