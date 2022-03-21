package com.jinproject.twomillustratedbook.Fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.jinproject.twomillustratedbook.Adapter.AlarmAdapter
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Database.Entity.Timer
import com.jinproject.twomillustratedbook.Item.*
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.Service.AlarmService
import com.jinproject.twomillustratedbook.Service.WService
import com.jinproject.twomillustratedbook.ViewModel.AlarmModel
import com.jinproject.twomillustratedbook.databinding.AlarmBinding
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import java.util.*
import kotlin.collections.ArrayList

class Alarm : Fragment() {
    var _binding:AlarmBinding ?=null
    val binding get() = _binding!!
    val timeModel: AlarmModel by viewModels()
    val bossModel: BookViewModel by activityViewModels(){ BookViewModelFactory((activity?.application as BookApplication).repository) }
    lateinit var adapter:AlarmAdapter
    lateinit var timerSharedPref:SharedPreferences
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 404
    private var listToWservice=ArrayList<Timer>()
    var day:Int=0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(requireActivity())) { // 다른앱 위에 그리기 체크
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+requireActivity().packageName))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            }
        }
        _binding=AlarmBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timerSharedPref=requireActivity().getSharedPreferences("TimerSharedPref", Context.MODE_PRIVATE)
        binding.alarmRecyclerView.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        adapter= AlarmAdapter()
        binding.alarmRecyclerView.adapter=adapter
        val alarmDialog=Dialog(requireActivity())
        alarmDialog.setContentView(R.layout.alarm_currentlist_dialog)


        binding.spinnerType.adapter=ArrayAdapter<String>(requireActivity(),R.layout.support_simple_spinner_dropdown_item, // 보스타입지정 spinner
            ArrayList<String>().apply {
                add("네임드")
                add("보스")
                add("대형보스")})

        val cal= Calendar.getInstance()
        val alarmItem=ArrayList<AlarmItem>() // 보스몹들의 개체값들을 가져와서 리스트에 담음

        binding.spinnerType.setSelection(timerSharedPref.getInt("lastSelectedSpinnerType",0))
        binding.spinnerType.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
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
                    val nameSpAdapter=ArrayAdapter(requireActivity(), R.layout.support_simple_spinner_dropdown_item,list)
                    binding.spinnerMons.adapter=nameSpAdapter
                })
                timerSharedPref.edit().putInt("lastSelectedSpinnerType",binding.spinnerType.selectedItemPosition).apply()
                timerSharedPref.edit().putInt("lastSelectedSpinnerMons",binding.spinnerMons.selectedItemPosition).apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.timerInput.setOnClickListener { // dialog 불러오기
            val picker=MyTimePicker()
            picker.setOnTimeListener(){hour, min, sec ->  
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,min)
                cal.set(Calendar.SECOND,sec)
            }
            picker.show(requireActivity().supportFragmentManager,"timePicker")
        }

        binding.timerStart.setOnClickListener { // dialog에넣은 시,분값 과 데이터베이스에있는 젠타임으로 타이머설정하고, 젠타임계산해서 데이터베이스에저장
            day=cal.get(Calendar.DAY_OF_WEEK)
            var hour=cal.get(Calendar.HOUR_OF_DAY)+((alarmItem[binding.spinnerMons.selectedItemPosition].gtime/60)/60)
            var min=cal.get(Calendar.MINUTE)+((alarmItem[binding.spinnerMons.selectedItemPosition].gtime/60)%60)
            var sec=cal.get(Calendar.SECOND)+((alarmItem[binding.spinnerMons.selectedItemPosition].gtime%60))
            while(sec>=60){
                min+=1
                sec-=60
            }
            while(min>=60){
                hour+=1
                min-=60
            }
            while(hour>=24){
                hour-=24
                day+=1
            }
            timeModel.setAlarm(cal.get(Calendar.HOUR_OF_DAY),cal.get(Calendar.MINUTE),alarmItem[binding.spinnerMons.selectedItemPosition])
            bossModel.setTimer(day,hour,min,sec,binding.spinnerMons.selectedItem.toString(),1)
        }

        binding.overlayAdd.setOnClickListener {
            getSettingDrawOverlays(1,binding)
        }
        binding.overlayDelete.setOnClickListener {
            getSettingDrawOverlays(0,binding)
        }

        binding.overlayOnoff.setOnClickListener{
            if(!timerSharedPref.getBoolean("flag",false)){
                timerSharedPref.edit().putBoolean("flag",true).apply()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
                    if (!Settings.canDrawOverlays(requireActivity())) { // 다른앱 위에 그리기 체크
                        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+requireActivity().packageName))
                        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
                    } else {
                        if(listToWservice.isNotEmpty()){
                            requireActivity().startService(Intent(activity, WService::class.java).apply { putExtra("list",listToWservice) })
                        }
                        else{
                            requireActivity().startService(Intent(activity, WService::class.java))
                        }
                    }
                }
            }
            else{
                timerSharedPref.edit().putBoolean("flag",false).apply()
                requireActivity().stopService(Intent(activity, WService::class.java))
            }

        }

        // 타이머가 등록된것이 있는지없는지 데이터베이스를 observer 로 구독하여 변동이생기면 뷰를 갱신함
        bossModel.timer.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            adapter.notifyDataSetChanged()
            if(adapter.itemCount==0){ requireActivity().stopService(Intent(requireActivity(),
                AlarmService::class.java))}

            listToWservice.clear()
            for(item in it){
                if(item.timer_ota==1){
                    listToWservice.add(item)
                }
            }
            Collections.sort(listToWservice, object :Comparator<Timer>{ // 일-시-분-초 순으로 정렬
                override fun compare(p0: Timer?, p1: Timer?): Int {
                    if(p0!!.timer_day.compareTo(p1!!.timer_day)==0){
                        if(p0.timer_hour.compareTo(p1.timer_hour)==0){
                            if(p0.timer_min.compareTo(p1.timer_min)==0){
                                if(p0.timer_sec.compareTo(p1.timer_sec)==0){
                                    return p0.timer_name.compareTo(p1.timer_name)
                                }
                                return p0.timer_sec.compareTo(p1.timer_sec)
                            }
                            return p0.timer_min.compareTo(p1.timer_min)
                        }
                        return p0.timer_hour.compareTo(p1.timer_hour)
                    }
                    return p0.timer_day.compareTo(p1.timer_day)
                }
            })
            if(listToWservice.isNotEmpty()){ //비어잇는게 아니면 백그라운드상에 동작하도록 서비스시작
                requireActivity().startService(Intent(activity, WService::class.java).apply { putExtra("list",listToWservice) })
            }
            else{ // 비어잇으면(등록된타이머가없으면) 현재시간만 계속 출력하도록 함
                requireActivity().startService(Intent(activity, WService::class.java))
            }
        })

        adapter.setItemClickListener(object:OnItemClickListener{ // 현재진행중인 알람 클릭시 나타나는 리스너
            override fun OnHomeItemClick(v: View, pos: Int) {
                val item=adapter.getItem(pos)
                alarmDialog.findViewById<Button>(R.id.alarm_cancel).setOnClickListener{
                    alarmDialog.cancel()
                }
                alarmDialog.findViewById<Button>(R.id.alarm_delete).setOnClickListener {
                    bossModel.setTimer(0,0,0,0,item.name,0)
                    var code=0
                    for(value in alarmItem){
                        if(value.name==item.name){
                            code=value.code
                        }
                    }
                    timeModel.clearAlarm(code)
                    timeModel.clearAlarm(code+300)
                    alarmDialog.cancel()
                }
                alarmDialog.setCanceledOnTouchOutside(true)
                alarmDialog.setCancelable(true)
                alarmDialog.show()
            }
        })
    }


    fun getSettingDrawOverlays(ota:Int,binding: AlarmBinding){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(requireActivity())) {              // 다른앱 위에 그리기 체크
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+requireActivity().packageName))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            }
            else{
                bossModel.setOta(ota,binding.spinnerMons.selectedItem.toString())
            }
        }
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}