package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.R
import java.util.*
import kotlin.collections.ArrayList

class Timer : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinnerType.adapter= ArrayAdapter<String>(requireActivity(), R.layout.support_simple_spinner_dropdown_item, // 보스타입지정 spinner
            ArrayList<String>().apply {
                add("네임드")
                add("보스")
                add("대형보스")})

        val cal= Calendar.getInstance()
        val alarmItem=ArrayList<AlarmItem>() // 보스몹들의 개체값들을 가져와서 리스트에 담음

        binding.spinnerType.setSelection(timerSharedPref.getInt("lastSelectedSpinnerType",0))
        binding.spinnerType.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var bossType=""
                when(p2){
                    0->bossType="named"
                    1->bossType="boss"
                    2->bossType="bigboss"
                }
                bossModel.getNameSp(bossType).observe(viewLifecycleOwner, Observer{// 지정된 보스타입에따른 보스몹리스트
                    val list=ArrayList<String>()
                    alarmItem.clear()
                    for(v in it){
                        list.add(v.mons_name)
                        alarmItem.add(AlarmItem(v.mons_name,v.mons_imgName,v.mons_Id,v.mons_gtime))
                    }
                    val nameSpAdapter= ArrayAdapter(requireActivity(), R.layout.support_simple_spinner_dropdown_item,list)
                    binding.spinnerMons.adapter=nameSpAdapter
                })
                timerSharedPref.edit().putInt("lastSelectedSpinnerType",binding.spinnerType.selectedItemPosition).apply()
                timerSharedPref.edit().putInt("lastSelectedSpinnerMons",binding.spinnerMons.selectedItemPosition).apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }
}