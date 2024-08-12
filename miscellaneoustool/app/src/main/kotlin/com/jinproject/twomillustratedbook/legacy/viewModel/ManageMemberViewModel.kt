package com.jinproject.twomillustratedbook.legacy.viewModel

/*
@HiltViewModel
open class ManageMemberViewModel @Inject constructor(@ApplicationContext private val context:Context) : ViewModel() {
    var mutableLogFlag = MutableLiveData(false) //1이면 로그인성공 ,2이면 로그인실패
    val logFlag: LiveData<Boolean> get() = mutableLogFlag
    protected val db = FirebaseDatabase.getInstance().reference
    protected val loginPreference = context.getSharedPreferences("login", Context.MODE_PRIVATE)
    val roomInfo by lazy { RoomInfo(ObservableField(""),ObservableField(""), ObservableField("")) }

}*/