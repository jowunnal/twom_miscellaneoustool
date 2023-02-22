package com.jinproject.twomillustratedbook.ui.screen.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.HomeBinding
import com.jinproject.twomillustratedbook.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : BaseFragment<HomeBinding>(){

    override fun getViewDataBinding(): HomeBinding = HomeBinding.inflate(layoutInflater)
    override var bottomNavigationBarVisibility: Boolean = true

    @SuppressLint("SetTextI18n")
    override fun initState() = with(binding) {
        tvInfo.text = "***패치버전 ver1.7***\n***추가사항***\n\n\n" +
                "1. 사용자편의성 개선\n - 전체적인 UI가 개선되었습니다.\n - 기기비율에 짤리지 않도록 수정되었습니다.\n\n\n" +
                "2. 서버기능이 삭제 되었습니다.\n - 사용량이 저조하여 삭제됩니다.\n\n\n" +
                "3. 2023.02.21 기준 신규몬스터, 도감에 대응되었습니다.\n" +
                " - 기간제 도감은 포함되지 않습니다.\n\n\n"
    }
}