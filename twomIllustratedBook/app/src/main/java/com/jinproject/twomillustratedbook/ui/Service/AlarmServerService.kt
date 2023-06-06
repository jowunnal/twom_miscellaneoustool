package com.jinproject.twomillustratedbook.ui.Service

/*
@AndroidEntryPoint
class AlarmServerService :LifecycleService() {
    lateinit var monster: Monster
    @Inject lateinit var timerRepository: TimerRepository
    @Inject lateinit var dropListRepository: DropListRepository
        override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        val db = FirebaseDatabase.getInstance().reference
        val loginPreference=applicationContext.getSharedPreferences("login", MODE_PRIVATE)
        db.child("RoomList").child(loginPreference.getString("key","")!!).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val alarmManager: AlarmManager = applicationContext.getSystemService(ALARM_SERVICE) as AlarmManager

                val type= object : GenericTypeIndicator<Room>(){}
                val data=snapshot.getValue(type)

                try{for(value in data?.roomBossList!!){
                    var count=value.hour*3600+value.min*60+value.sec
                    val getTime= SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis())) // simpledateformat의 hh는0-11, HH는0-23 이다.
                    val nowTime=getTime.split(":")
                    count-=(Integer.parseInt(nowTime[0])*3600+ Integer.parseInt(nowTime[1])*60+Integer.parseInt(nowTime[2]))
                    if(count>0){
                        lifecycleScope.launch(Dispatchers.IO){
                            monster= withContext(Dispatchers.IO) { dropListRepository.getMonsInfo(value.name) }
                            val timerSharedPreferences=applicationContext.getSharedPreferences("TimerSharedPref",
                                MODE_PRIVATE
                            )
                            val first=timerSharedPreferences.getInt("first",5)
                            val last=timerSharedPreferences.getInt("last",0)
                            makeAlarm(alarmManager,application,(count-first*60)*1000, AlarmItem(
                                monster.monsName,
                                monster.monsImgName,
                                getMonsterCode(monster.monsName),
                                monster.monsGtime)
                            )

                            makeAlarm(alarmManager,application,(count-last*60)*1000, AlarmItem(
                                monster.monsName,
                                monster.monsImgName,
                                getMonsterCode(monster.monsName)+300,
                                monster.monsGtime)
                            )
                            timerRepository.setTimer(value.day,value.hour,value.min,value.sec,value.name)
                        }
                    }
                }}catch (e:NullPointerException){
                    Toast.makeText(applicationContext,"서버상에 등록된 보스목록이 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return super.onBind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        applicationContext.getSharedPreferences("login", MODE_PRIVATE).edit().putBoolean("statue",false).apply()
    }

    private fun makeAlarm(alarmManager: AlarmManager, app: Application, count:Int, item: AlarmItem){
        val notifyIntentImmediately = Intent(app, AlarmService::class.java)
        notifyIntentImmediately.putExtra("msg",item.name)
        notifyIntentImmediately.putExtra("img",item.imgName)
        notifyIntentImmediately.putExtra("code",item.code)
        notifyIntentImmediately.putExtra("gtime",item.gtime)
        val notifyPendingIntent = PendingIntent.getService(app,item.code,notifyIntentImmediately,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(System.currentTimeMillis()+count,notifyPendingIntent), notifyPendingIntent)
    }
}
 */