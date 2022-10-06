package com.jinproject.twomillustratedbook.Fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.jinproject.twomillustratedbook.Adapter.AlarmAdapter
import com.jinproject.twomillustratedbook.Adapter.AlarmSelectedAdapter
import com.jinproject.twomillustratedbook.Database.Entity.Timer
import com.jinproject.twomillustratedbook.Item.*
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.Service.AlarmServerService
import com.jinproject.twomillustratedbook.Service.WService
import com.jinproject.twomillustratedbook.databinding.AlarmBinding
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding
import com.jinproject.twomillustratedbook.listener.OnBossNameClickedListener
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import com.jinproject.twomillustratedbook.utils.calculateTimer
import com.jinproject.twomillustratedbook.utils.getMonsterCode
import com.jinproject.twomillustratedbook.utils.sortTimerList
import com.jinproject.twomillustratedbook.viewModel.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class Alarm : BindFragment<AlarmBinding>(R.layout.alarm,true){
    val alarmViewModel: AlarmViewModel by viewModels()
    val timerViewModel: TimerViewModel by viewModels()
    val serverManageViewModel : ServerManageViewModel by viewModels()
    val dropListViewModel: DropListViewModel by activityViewModels()
    val adapter:AlarmAdapter by lazy { AlarmAdapter() }
    val selectedAdapter by lazy{AlarmSelectedAdapter()}
    val cal: Calendar by lazy { Calendar.getInstance() }
    val alarmDialog by lazy { Dialog(requireActivity()) }
    lateinit var timerSharedPref:SharedPreferences
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 404
    lateinit var navController:NavController
    private var mRewardedAd: RewardedAd? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        binding.serverManageViewModel=serverManageViewModel
        binding.alarmViewModel=alarmViewModel
        navController=Navigation.findNavController(view)
        timerSharedPref=requireActivity().getSharedPreferences("TimerSharedPref", Context.MODE_PRIVATE)
        binding.alarmRecyclerView.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.alarmRecyclerView.adapter=adapter
        alarmDialog.setContentView(R.layout.alarm_currentlist_dialog)
        binding.alarmSelectedList.adapter=selectedAdapter
        binding.alarmSelectedList.layoutManager=GridLayoutManager(requireActivity(),2,GridLayoutManager.VERTICAL,false)

        dropListViewModel.selectedBossList.observe(viewLifecycleOwner,Observer{
            if(it!=null){
                selectedAdapter.setItems(it)
                selectedAdapter.notifyDataSetChanged()
            }
        })

        selectedAdapter.setClickListener(object : OnBossNameClickedListener{
            override fun setOnItemClickListener(
                v: View,
                pos: Int,
                binding: AlarmUserSelectedItemBinding
            ) {
                CoroutineScope(Dispatchers.Main).launch {
                    dropListViewModel.checkIsClickedBoss(pos,selectedAdapter.getItem(pos))
                    binding.alarmUserSelectedItemSwitch.setOnCheckedChangeListener { _, b ->
                        when(b){
                            true->getSettingDrawOverlays(1,binding.alarmUserSelectedItem.text.toString())
                            false->getSettingDrawOverlays(0,binding.alarmUserSelectedItem.text.toString())
                        }
                    }
                }
            }
        })


        binding.timerInput.setOnClickListener { // dialog 불러오기
            val picker=MyTimePicker()
            picker.setOnTimeListener(){hour, min, sec ->  
                cal.set(Calendar.HOUR_OF_DAY,hour)
                cal.set(Calendar.MINUTE,min)
                cal.set(Calendar.SECOND,sec)
            }
            picker.show(requireActivity().supportFragmentManager,"timePicker")
        }

        RewardedAd.load(requireActivity(),"ca-app-pub-3630394418001517/7487969905", AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mRewardedAd = null
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                mRewardedAd = rewardedAd
            }
        })
        mRewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdClicked() {
                // Called when a click is recorded for an ad.
            }

            override fun onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                mRewardedAd=null
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                // Called when ad fails to show.
                mRewardedAd=null
            }

            override fun onAdImpression() {
                // Called when an impression is recorded for an ad.
            }

            override fun onAdShowedFullScreenContent() {
                // Called when ad is shown.
            }
        }

        fun showRewaredAd(mRewardedAd:RewardedAd?){
            mRewardedAd?.show(requireActivity(), OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    var rewardAmount = rewardItem.amount
                    var rewardType = rewardItem.type
                }
            })
        }

        binding.timerStart.setOnClickListener { // dialog에넣은 시,분값 과 데이터베이스에있는 젠타임으로 타이머설정하고, 젠타임계산해서 데이터베이스에저장
            showRewaredAd(mRewardedAd)
            try {
                alarmViewModel.setAlarm(
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    dropListViewModel.alarmItem
                )
                timerViewModel.deleteTimer(dropListViewModel.monster.monsName)
                timerViewModel.setTimer(calculateTimer(cal,dropListViewModel.monster))
            }catch (e:kotlin.UninitializedPropertyAccessException){
                Toast.makeText(requireActivity(),"먼저 보스를 선택해주세요!",Toast.LENGTH_SHORT).show()
            }
        }

        // 타이머가 등록된것이 있는지없는지 데이터베이스를 observer 로 구독하여 변동이생기면 뷰를 갱신함
        timerViewModel.timer.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            adapter.notifyDataSetChanged()
            Log.d("test",it.toString())

            serverManageViewModel.setServerBossList(it)
            serverManageViewModel.setTimerOtaList(it)
        })


        adapter.setItemClickListener(object:OnItemClickListener{ // 현재진행중인 알람 클릭시 나타나는 리스너
            override fun OnHomeItemClick(v: View, pos: Int) {
                val item=adapter.getItem(pos)
                alarmDialog.findViewById<Button>(R.id.alarm_cancel).setOnClickListener{
                    alarmDialog.cancel()
                }
                alarmDialog.findViewById<Button>(R.id.alarm_delete).setOnClickListener {
                    timerViewModel.deleteTimer(item.name)
                    var code=0
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO){code=getMonsterCode(dropListViewModel.getMonsInfo(item.name).monsName)}
                        alarmViewModel.clearAlarm(code)
                        alarmViewModel.clearAlarm(code+300)
                        alarmDialog.cancel()
                    }
                }
                alarmDialog.setCanceledOnTouchOutside(true)
                alarmDialog.setCancelable(true)
                alarmDialog.show()
            }
        })



        binding.serverOutput.setOnClickListener {
            serverManageViewModel.setServerOutput()
        }

        binding.serverOnoff.setOnClickListener {
            binding.serverStatueTv.text=serverManageViewModel.setServerOnOff()
        }
    }


    fun getSettingDrawOverlays(ota:Int,mons_name:String){
        if(checkAuthorityDrawOverlays())
            timerViewModel.setOta(ota,mons_name)
    }

    fun checkAuthorityDrawOverlays():Boolean{ // 다른앱 위에 그리기 체크 : true = 권한있음 , false = 권한없음
        if (!Settings.canDrawOverlays(requireActivity())) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+requireActivity().packageName))
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            return false
        }
        else{
            return true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.timer_option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.icon_fix->navController.navigate(R.id.action_alarm_to_timer)

            R.id.icon_login->navController.navigate(R.id.action_alarm_to_login)

            R.id.icon_addTime->if(!timerSharedPref.getBoolean("flag",false)){
                timerSharedPref.edit().putBoolean("flag",true).apply()
                if (checkAuthorityDrawOverlays()) {
                    if(serverManageViewModel.listToWservice.isNotEmpty()){
                        requireActivity().startService(Intent(activity, WService::class.java).apply { putExtra("list",serverManageViewModel.listToWservice) })
                    }
                    else{
                        requireActivity().startService(Intent(activity, WService::class.java))
                    }
                }
            }
            else{
                timerSharedPref.edit().putBoolean("flag",false).apply()
                requireActivity().stopService(Intent(activity, WService::class.java))
            }


        }
        return super.onOptionsItemSelected(item)
    }
}