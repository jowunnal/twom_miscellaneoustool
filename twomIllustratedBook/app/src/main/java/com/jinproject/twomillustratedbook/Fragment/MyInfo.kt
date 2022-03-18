package com.jinproject.twomillustratedbook.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jinproject.twomillustratedbook.databinding.MyinfoBinding

class MyInfo : Fragment() {
    var _binding:MyinfoBinding ?=null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= MyinfoBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvInfo.text="***패치버전 ver1.5.2***\n***추가사항***\n" +
                "- 알람이 5분전 단일알람에서 5분전,0분전 두가지로 변경되었습니다.\n"+
                "- 화면상단의 등록된 몬스터에대한 타이머출력이 일-시-분-초 순으로 정렬됩니다.\n"+
                "- 화면상단에 현재시간항상출력이 추가되었습니다. 개별적으로 on/off할수없습니다.\n"+
                "- 도감부분의 UI가 개선되었습니다. 아이탬개수가 1개일때는 더이상표기되지 않습니다.\n"+
                "- 더이상 특정기기에서 짤리는부분이 발생하지않습니다.\n"+
                "- 알람부분의 버튼이름들이 더 직관적으로 변경되었습니다.\n\n\n"+
                "***패치버전 ver1.5.0***\n***추가사항***\n" +
                "- 드랍몹(대형보스급)빅마마,우크파나,바슬라프,일루스트,마녀딜린 추가\n" +
                "- 알람화면 UI구성 추가 (화면중앙에 젠타임표기&제거&전체ON/OFF)\n"+
                "- 날짜단위 추가 및 24시 넘어갓던버그해결\n"+
                "- 현재진행중인 알람내역 요일-시-분-초 순으로 정렬기능추가\n"+
                "- 특정기기의 UI 가로넓이 짤리는부분 개선\n\n\n\n\n"+
                "***사용법***\n"+
                "1. 각 도감과 드랍아이템에서 오른쪽상단의 돋보기버튼클릭후 검색가능\n"+
                "1-2. 도감의경우 스텟으로 검색시 명칭을정확히 입력 예)pvp데미지\n\n"+
                "2. 알람설정시 맨위부터 몬스터타입 선택후 몬스터 선택\n"+
                "2-1. 컷타임입력 후 타이머 시작하기 버튼 누를시 타이머등록\n"+
                "2-2. 컷타임입력버튼 미클릭시 현재시간을 기준으로 타이머등록가능\n\n"+
                "3. 상단중앙에 현재진행중인 알람과 현재시간을 항상출력하는방법\n"+
                "3-1. 몬스터타입 과 몬스터이름 선택후 등록하기 버튼클릭\n"+
                "3-2. 화면상단중앙에 표시가능해짐.\n"+
                "3-3. 더이상 표시하고싶지 않는 몬스터에 대해서 몬스터타입,이름선택후\n"+
                "3-4. 해제하기 버튼클릭 -> 더이상 화면상단중앙에 해당몬스터 표시안함\n"+
                "3-5. 개별적으로 해제하지않고 전체에 대해서 일시적으로 화면에표시하고싶지않은경우 ON/OFF버튼클릭\n"+
                "3-6. 현재시간항상출력은 개별적으로 끄거나 켤수 없습니다.\n\n"+
                "*몬스터들의 변동젠타임(점검이후 줄어드는변동타임)은미구현으로 고정젠타임 입니다.*"
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}