package com.jinproject.twomillustratedbook.ui.listener

import android.view.View
import com.jinproject.twomillustratedbook.domain.Item.CheckStatue
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding

interface OnBossNameClickedListener {
    fun setOnItemClickListener(v:View,pos:Int,binding:AlarmUserSelectedItemBinding)
}