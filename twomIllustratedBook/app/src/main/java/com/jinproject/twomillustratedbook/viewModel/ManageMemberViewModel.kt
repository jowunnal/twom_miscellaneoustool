package com.jinproject.twomillustratedbook.viewModel
import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import com.jinproject.twomillustratedbook.Item.RoomInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
open class ManageMemberViewModel @Inject constructor(@ApplicationContext private val context:Context) : ViewModel() {
    var mutableLogFlag = MutableLiveData(false) //1이면 로그인성공 ,2이면 로그인실패
    val logFlag: LiveData<Boolean> get() = mutableLogFlag
    protected val db = FirebaseDatabase.getInstance().reference
    protected val loginPreference = context.getSharedPreferences("login", Context.MODE_PRIVATE)
    val roomInfo by lazy { RoomInfo(ObservableField(""),ObservableField(""), ObservableField("")) }

}