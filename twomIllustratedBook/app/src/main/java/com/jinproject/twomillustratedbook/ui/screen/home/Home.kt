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
        tvInfo.text = "***패치버전 ver1.6***\n***추가사항***\n" +
                "1. 사용자편의성 개선\n -알람을 사용자가 원하는대로 (0~59분)마다 울리도록 설정가능합니다.\n" +
                " -전체적인 화면구성이 변경되었습니다.\n" +
                " -알람화면의 상단메뉴는 체크아이콘=현재시간보기, 시계아이콘=알람커스텀화면이동, 로그인아이콘=로그인화면이동 입니다.\n\n\n" +
                "2. 서버기능이 추가되었습니다.\n" +
                " -1)알람화면 상단의 로그인메뉴를 선택합니다.\n" +
                " -2)아이디,비밀번호,권한코드를 입력한후 방생성 버튼을 클릭합니다.\n" +
                " -3)로그인버튼을 클릭합니다.\n" +
                " -4)컷타임입력과 타이머등록을 통해 보스타이머를 모두 등록하신뒤, 서버로 내보내기버튼을 클릭합니다.\n" +
                " -5)서버on/off 버튼을 눌러 서버로부터 데이터를 전달받습니다.\n\n" +
                " <주의> 권한코드가 일치하지않는다면, 서버로 타이머등록이 불가능합니다.\n 일치하지않는다면 로그인화면으로 이동후 재로그인이 필요하며, 정확한 권한코드입력을 해주셔야합니다.\n" +
                " 또한, 타이머등록 권한이 없으신 일반사용자들은 서버로내보내기 필요없이 서버on/off버튼을 통해 온라인모드로 설정해두셔야 서버로부터 데이터를 받을 수 있습니다.\n" +
                " 또한, 어플을 강제종료 혹은 서비스중지를 하게되면 서버로부터 데이터를 받을수 없습니다.\n\n\n" +
                "3. 알람커스텀 기능이 개선되었습니다.\n" +
                "-1)알람화면 상단의 커스텀메뉴를 선택합니다.\n" +
                "-2)스피너를 통해 원하는 보스를 선택한뒤, 추가하기 버튼을 클릭합니다.\n" +
                "-3)필요하지않은 보스의경우 보스이름이 표시된 버튼을 클릭하여 삭제할수 있습니다.\n" +
                "-4)뒤로가기 버튼을 눌러 알람화면으로 이동합니다.\n" +
                "-5)알람화면에서 보스이름이 표시된 버튼을 클릭하면, 보스이름이 빨간색 으로 변경되며, 이는 선택되어있음을 의미합니다.\n" +
                "-6)보스를 선택하셧다면, 컷타임입력 버튼을눌러 컷타임을 입력합니다.(미입력시 현재시간이 입력됨)\n" +
                "-7)타이머 등록 버튼을 누르면 타이머가 등록됩니다.\n" +
                "-8) 기존의 5분전,0분전 으로 고정되어 울리던 타이머의 간격을 커스터마이징 할수있도록 변경되었습니다.\n" +
                "-9) 커스텀 화면 하단의 두개의 숫자를 통해 0~59중 원하는 숫자를 만들어줍니다.\n" +
                "-10) 만들어진 숫자는 '분' 단위 이며, 첫알람선택과 마지막알람선택 버튼을 클릭하여, 원하는 두가지의 알람간격을 설정 할수있습니다. (미등록시 기존의 5분,0분전 의간격으로 설정)\n\n\n" +
                "화면의 기기마다 잘리는 이슈가 해결되지 않았습니다. 추후 수정될 예정입니다.\n추가적인 개선사항 및 피드백은 메일 혹은 리뷰를통해 남겨주시면 반영해보도록 하겠습니다.\n"
    }
}