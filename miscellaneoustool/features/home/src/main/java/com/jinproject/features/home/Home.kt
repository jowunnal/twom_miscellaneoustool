package com.jinproject.features.home

import android.annotation.SuppressLint
import android.view.*
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.features.core.BaseFragment
import com.jinproject.features.home.databinding.HomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : BaseFragment<HomeBinding>(){

    override fun getViewDataBinding(): HomeBinding = HomeBinding.inflate(layoutInflater)
    override var bottomNavigationBarVisibility: Boolean = true

    @SuppressLint("SetTextI18n")
    override fun initState() = with(binding) {
        tvInfo.text = this@Home.requireContext().doOnLocaleLanguage(
            onKo = "\n\n\n" +
                    """
                    # 기능 구현
                    
                    1. 도감 커스터마이징 기능이 추가되었습니다.
                        - 아이템도감에서 화면을 꾹 누르면 다중선택이 되고, 상단의 휴지통 아이콘으로 제거할 수 있습니다.
                        - 뒤로가기 혹은 상단의 뒤로가기버튼 을 누르면 해제됩니다.
                        - 설정아이콘을 누르면 제거한 도감을 돌리는 '필터링 설정 변경'과 아이템 설정 변경을 이용할 수 있습니다.
                        - 아이템 설정 변경에서는 아이템의 가격을 개수 당 가격으로 설정할 수 있습니다.
                        
                    2. 몬스터도감이 격자형 구조로 변경되었습니다.
                    
                    3. 현재시간 항상 보기 기능에서 위치를 원하는 대로 이동시킬 수 있습니다.
                    
                    4. 인앱결제가 추가되었습니다.
                        - 광고제거 구매시 상단광고와 알람시 발생하는 전면광고가 제거됩니다.
                        - 개발자에게 후원을 할 수 있습니다.
                        
                    5. 길드 마크 심볼 만들기 기능이 추가되었어요.
                        - 하단바의 G 아이콘을 클릭하면 길드 마크 심볼 생성하기 화면으로 이동 가능해요.
                        - 특정 이미지를 선택하여 12*12 픽셀 형태로 변환해 드리는 기능이에요.
                        - 해당 콘텐츠는 소액의 과금이 발생하니, 반드시 제공되는 미리보기를 확인한 후 진행해 주세요.

                    # ${getString(com.jinproject.design_ui.R.string.version)} 수정
                    - 현재시간 항상 보기 서비스가 안드로이드 14 버전에 대응되도록 수정되었어요.
                    
                """.trimIndent(),
            onElse = """
                # Intro
                
                1. Collection List
                    - You can check collection list which means how many stuffs or price you need
                    - If u had collected some collection, then you can remove it
                    - Also you can customize the price of item or rollback collection what you had removed
                    - You can search collections using Search Menu on Top bar
               
                2. Monster List
                    - You can check monster list on second menu
                    - You can search items using Search Menu on Top bar
                    
                3. Alarm
                    - You can set a monster's alarm, just put the time when you killed it
                    - And you can check them on list downside or you can use Overlay operation
                    - First menu which means Setting in alarm screen, you can adjust alarm interval
                    - Second menu which means Overlay in alarm screen, you can see the current times on screen
                    - And you can adjust font size or move the overlay everywhere u want
                    - in the downside you can add monster's alarm on overlay
                    - finally you can remove advertisement by supporting developer
                    
                Thanks for using App! Enjoy it
                
                # On ${getString(com.jinproject.design_ui.R.string.version)}
                    - Updated about overlayService target on Android 14
            """.trimIndent()
        )
    }
}