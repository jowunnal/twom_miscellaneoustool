package com.jinproject.twomillustratedbook.Fragment

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.jinproject.twomillustratedbook.Database.BookApplication
import com.jinproject.twomillustratedbook.Database.Entity.Monster
import com.jinproject.twomillustratedbook.Database.Entity.Timer
import com.jinproject.twomillustratedbook.Item.*
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.Service.AlarmServerService
import com.jinproject.twomillustratedbook.Service.WService
import com.jinproject.twomillustratedbook.ViewModel.AlarmModel
import com.jinproject.twomillustratedbook.ViewModel.AlarmPresenter
import com.jinproject.twomillustratedbook.databinding.AlarmBinding
import com.jinproject.twomillustratedbook.databinding.AlarmUserSelectedItemBinding
import com.jinproject.twomillustratedbook.listener.OnBossNameClickedListener
import com.jinproject.twomillustratedbook.listener.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class Alarm : Fragment() {
    var _binding:AlarmBinding ?=null
    val binding get() = _binding!!
    val timeModel: AlarmModel by viewModels()
    val bossModel: BookViewModel by activityViewModels()
    val adapter:AlarmAdapter by lazy { AlarmAdapter() }
    val selectedAdapter by lazy{AlarmSelectedAdapter()}
    lateinit var timerSharedPref:SharedPreferences
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 404
    private var listToWservice=ArrayList<Timer>()
    lateinit var navController:NavController
    private var serverBossList=ArrayList<TimerItem>()
    private var mRewardedAd: RewardedAd? = null
    @Inject lateinit var alarmPresenter: AlarmPresenter

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

        navController=Navigation.findNavController(view)
        timerSharedPref=requireActivity().getSharedPreferences("TimerSharedPref", Context.MODE_PRIVATE)
        binding.alarmRecyclerView.layoutManager=LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false)
        binding.alarmRecyclerView.adapter=adapter
        val alarmDialog=Dialog(requireActivity())
        alarmDialog.setContentView(R.layout.alarm_currentlist_dialog)

        binding.alarmSelectedList.adapter=selectedAdapter
        binding.alarmSelectedList.layoutManager=GridLayoutManager(requireActivity(),2,GridLayoutManager.VERTICAL,false)
        val selectedBossList=requireActivity().getSharedPreferences("bossList",Context.MODE_PRIVATE)
        val boss=selectedBossList.getStringSet("boss", mutableSetOf("불도저"))
        val list=ArrayList<String>()
        list.addAll(boss!!)
        selectedAdapter.setItems(list)
        selectedAdapter.notifyDataSetChanged()
        setHasOptionsMenu(true)
        val cal= Calendar.getInstance()

        selectedAdapter.setClickListener(object : OnBossNameClickedListener{
            override fun setOnItemClickListener(
                v: View,
                pos: Int,
                binding: AlarmUserSelectedItemBinding
            ) {
                bossModel.checkIsClickedBoss(pos,selectedAdapter.getItem(pos))
                binding.alarmUserSelectedItemSwitch.setOnCheckedChangeListener { _, b ->
                    when(b){
                        true->getSettingDrawOverlays(1,binding.alarmUserSelectedItem.text.toString())
                        false->getSettingDrawOverlays(0,binding.alarmUserSelectedItem.text.toString())
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
        var adRequest = AdRequest.Builder().build()

        RewardedAd.load(requireActivity(),"ca-app-pub-3630394418001517/7487969905", adRequest, object : RewardedAdLoadCallback() {
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
                timeModel.setAlarm(
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    bossModel.alarmItem
                )
                bossModel.setTimer(bossModel.calculateTimer(cal))
            }catch (e:kotlin.UninitializedPropertyAccessException){
                Toast.makeText(requireActivity(),"먼저 보스를 선택해주세요!",Toast.LENGTH_SHORT).show()
            }
        }

        // 타이머가 등록된것이 있는지없는지 데이터베이스를 observer 로 구독하여 변동이생기면 뷰를 갱신함
        bossModel.timer.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it)
            adapter.notifyDataSetChanged()
            serverBossList.clear()
            for(data in it){
                serverBossList.add(TimerItem(data.timerMonsName,data.day,data.hour,data.min,data.sec))
            }

            listToWservice.clear()
            for(item in it){
                if(item.ota==1){
                    listToWservice.add(item)
                }
            }
            bossModel.sortTimerList(listToWservice)
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
                    bossModel.setTimer(bossModel.calculateTimer(cal))
                    var code=0
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO){code=alarmPresenter.getMonsterCode(bossModel.getMonsInfo(item.name).monsName)}
                        timeModel.clearAlarm(code)
                        timeModel.clearAlarm(code+300)
                        alarmDialog.cancel()
                    }
                }
                alarmDialog.setCanceledOnTouchOutside(true)
                alarmDialog.setCancelable(true)
                alarmDialog.show()
            }
        })


        val loginPreference=requireActivity().getSharedPreferences("login",Context.MODE_PRIVATE)
        binding.serverOutput.setOnClickListener {

            val db:DatabaseReference=FirebaseDatabase.getInstance().reference
            try {
                val id=loginPreference.getString("id","")!!
                val pw=loginPreference.getString("pw","")!!
                val key=loginPreference.getString("key","")!!
                val authorityCode=loginPreference.getString("authorityCode","")
                db.child("RoomList").child(key).get().addOnSuccessListener {
                    if(it.child("authorityCode").value==authorityCode){
                        db.child("RoomList").child(key).setValue(Room(id,pw,serverBossList,authorityCode))
                        Toast.makeText(requireActivity(),"서버에 등록이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(requireActivity(),"등록 권한이 없습니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e:kotlin.UninitializedPropertyAccessException){
                Toast.makeText(requireActivity(),"먼저 보스를 선택해주세요!",Toast.LENGTH_SHORT).show()
            }

        }
        binding.serverOnoff.setOnClickListener {
            when(loginPreference.getBoolean("statue",false)){
                false->{
                    requireActivity().startService(Intent(activity,AlarmServerService::class.java))
                    loginPreference.edit().putBoolean("statue",true).apply()
                    binding.serverStatueTv.text="서버상태: "+when(loginPreference.getBoolean("statue",false)){false->"오프라인" true->"온라인"}+
                            "/로그인ID: "+loginPreference.getString("id","")
                    Toast.makeText(requireActivity(),"서버로부터 데이터를 받기 시작합니다.",Toast.LENGTH_SHORT).show()
                }
                true->{
                    requireActivity().stopService(Intent(activity,AlarmServerService::class.java))
                    loginPreference.edit().putBoolean("statue",false).apply()
                    binding.serverStatueTv.text="서버상태: "+when(loginPreference.getBoolean("statue",false)){false->"오프라인" true->"온라인"}+
                            "/로그인ID: "+loginPreference.getString("id","")
                    Toast.makeText(requireActivity(),"서버로부터 데이터를 받지 않습니다.",Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.serverStatueTv.text="서버상태: "+when(loginPreference.getBoolean("statue",false)){false->"오프라인" true->"온라인"}+
                "/로그인ID: "+loginPreference.getString("id","")
    }


    fun getSettingDrawOverlays(ota:Int,mons_name:String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(requireActivity())) {              // 다른앱 위에 그리기 체크
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+requireActivity().packageName))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            }
            else{
                bossModel.setOta(ota,mons_name)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.timer_option_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.icon_fix->navController.navigate(R.id.action_alarm_to_timer)

            R.id.icon_addTime->if(!timerSharedPref.getBoolean("flag",false)){
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
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }
}