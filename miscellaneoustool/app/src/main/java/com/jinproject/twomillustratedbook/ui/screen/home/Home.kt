package com.jinproject.twomillustratedbook.ui.screen.home

import android.annotation.SuppressLint
import android.view.*
import com.jinproject.core.util.doOnLocaleLanguage
import com.jinproject.twomillustratedbook.databinding.HomeBinding
import com.jinproject.twomillustratedbook.ui.base.BaseFragment
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

                    # 2.1.0 수정안
                    - 전체적인 UX가 개선됬습니다.
                    - 성능 이슈 및 코드 안정화 작업이 수행됬습니다.
                    
                """.trimIndent(),
            onElse = """
                # New on ver.2.1.0
                - You can search collections with no more concerns about capital letter.
                - Alarm u set has been changed to shown by 12 clock.
                - Updated for being stable.
                
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
            """.trimIndent()
        )
    }
}