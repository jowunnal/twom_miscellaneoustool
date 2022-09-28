package com.jinproject.twomillustratedbook.Fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Item.BookViewModel
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectBinding
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jinproject.twomillustratedbook.Adapter.AlarmSelectedAdapter
import com.jinproject.twomillustratedbook.ViewModel.AlarmModel
import com.jinproject.twomillustratedbook.ViewModel.AlarmPresenter
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding
import com.jinproject.twomillustratedbook.listener.OnBossNameClickedListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Timer : Fragment() {
    var _binding:AlarmUserSelectBinding ?= null
    val binding get()=_binding!!
    lateinit var timerSharedPref: SharedPreferences
    val adapter:AlarmSelectedAdapter by lazy{AlarmSelectedAdapter()}
    val bossModel: BookViewModel by activityViewModels()
    val alarmModel:AlarmModel by viewModels()
    @Inject lateinit var alarmPresenter: AlarmPresenter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=AlarmUserSelectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.spinnerType.adapter= ArrayAdapter<String>(requireActivity(), R.layout.support_simple_spinner_dropdown_item, // 보스타입지정 spinner
            ArrayList<String>().apply {
                add("네임드")
                add("보스")
                add("대형보스")})

        val alarmItem=ArrayList<AlarmItem>() // 보스몹들의 개체값들을 가져와서 리스트에 담음

        timerSharedPref=requireActivity().getSharedPreferences("TimerSharedPref", Context.MODE_PRIVATE)
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
                        list.add(v.monsName)
                        alarmItem.add(AlarmItem(v.monsName,v.monsImgName,alarmPresenter.getMonsterCode(v.monsName),v.monsGtime))
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
        val selectedBossList=requireActivity().getSharedPreferences("bossList",Context.MODE_PRIVATE)

        binding.alarmUserSelectAdd.setOnClickListener{
            val bossList=ArrayList<String>()
            bossList.addAll(selectedBossList.getStringSet("boss", mutableSetOf("불도저"))!!)
            bossList.add(binding.spinnerMons.selectedItem.toString())
            selectedBossList.edit().putStringSet("boss",bossList.toSet()).apply()
            showBossList(selectedBossList)
        }
        binding.alarmUserSelectRecyclerView.adapter=adapter
        binding.alarmUserSelectRecyclerView.layoutManager=GridLayoutManager(requireActivity(),2,GridLayoutManager.HORIZONTAL,false)
        showBossList(selectedBossList)

        val alarmDialog= Dialog(requireActivity())
        alarmDialog.setContentView(R.layout.alarm_currentlist_dialog)
        adapter.setClickListener(object :OnBossNameClickedListener{
            override fun setOnItemClickListener(
                v: View,
                pos: Int,
                binding: AlarmUserSelectedItemBinding
            ) {
                val item=adapter.getItem(pos)
                alarmDialog.findViewById<Button>(R.id.alarm_cancel).setOnClickListener{
                    alarmDialog.cancel()
                }
                alarmDialog.findViewById<Button>(R.id.alarm_delete).setOnClickListener {
                    val bossList=ArrayList<String>()
                    bossList.addAll(selectedBossList.getStringSet("boss", mutableSetOf(""))!!)
                    bossList.remove(item)
                    selectedBossList.edit().putStringSet("boss",bossList.toSet()).apply()
                    showBossList(selectedBossList)
                    alarmDialog.cancel()
                }
                alarmDialog.setCanceledOnTouchOutside(true)
                alarmDialog.setCancelable(true)
                alarmDialog.show()
            }

        })

        binding.alarmUserSelectBack.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        binding.alarmRemainMin.maxValue=5
        binding.alarmRemainMin.minValue=0
        binding.alarmRemainSec.maxValue=9
        binding.alarmRemainSec.minValue=0

        val timerSharedPreferences=requireActivity().getSharedPreferences("TimerSharedPref",Context.MODE_PRIVATE)
        binding.alarmRemainFirst.setOnClickListener {
            val first=binding.alarmRemainMin.value.toString()
            val last=binding.alarmRemainSec.value.toString()
            val fl:String=first+last
            timerSharedPreferences.edit().putInt("first",Integer.parseInt(fl)).apply()
            Toast.makeText(requireActivity(),"첫 알람이 "+fl+" 분 후 마다 울리도록 세팅되었습니다.",Toast.LENGTH_SHORT).show()
        }
        binding.alarmRemainLast.setOnClickListener {
            val first=binding.alarmRemainMin.value.toString()
            val last=binding.alarmRemainSec.value.toString()
            val fl:String=first+last
            timerSharedPreferences.edit().putInt("last",Integer.parseInt(fl)).apply()
            Toast.makeText(requireActivity(),"마지막 알람이 "+fl+" 분 후 마다 울리도록 세팅되었습니다.",Toast.LENGTH_SHORT).show()
        }

    }
    fun showBossList(pref:SharedPreferences){
        val list=ArrayList<String>()
        list.addAll(pref.getStringSet("boss", mutableSetOf(""))!!)
        adapter.setItems(list)
        adapter.notifyDataSetChanged()
    }

}