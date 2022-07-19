package com.jinproject.twomillustratedbook.Fragment

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Item.AlarmItem
import com.jinproject.twomillustratedbook.Item.BookViewModel
import com.jinproject.twomillustratedbook.Item.BookViewModelFactory
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectBinding
import java.util.*
import kotlin.collections.ArrayList
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.jinproject.twomillustratedbook.Adapter.AlarmSelectedAdapter
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding
import com.jinproject.twomillustratedbook.listener.OnBossNameClickedListener

class Timer : Fragment() {
    var _binding:AlarmUserSelectBinding ?= null
    val binding get()=_binding!!
    lateinit var timerSharedPref: SharedPreferences
    val adapter:AlarmSelectedAdapter by lazy{AlarmSelectedAdapter()}
    val bossModel: BookViewModel by activityViewModels(){ BookViewModelFactory((activity?.application as BookApplication).repository) }
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

        val cal= Calendar.getInstance()
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
        val selectedBossList=requireActivity().getSharedPreferences("bossList",Context.MODE_PRIVATE)

        binding.alarmUserSelectAdd.setOnClickListener{
            val bossList=ArrayList<String>()
            bossList.addAll(selectedBossList.getStringSet("boss", mutableSetOf(""))!!)
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
    }
    fun showBossList(pref:SharedPreferences){
        val list=ArrayList<String>()
        list.addAll(pref.getStringSet("boss", mutableSetOf(""))!!)
        adapter.setItems(list)
        adapter.notifyDataSetChanged()
    }
}