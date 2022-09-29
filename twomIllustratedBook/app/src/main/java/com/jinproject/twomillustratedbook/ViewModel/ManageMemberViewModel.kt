package com.jinproject.twomillustratedbook.ViewModel
import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jinproject.twomillustratedbook.Item.User
import com.jinproject.twomillustratedbook.Repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
open class ManageMemberViewModel @Inject constructor(@ApplicationContext private val context:Context, private val repository: UserRepositoryImpl) : ViewModel() {
    var mutableLogFlag = MutableLiveData(0) //1이면 로그인성공 ,2이면 로그인실패
    val logFlag: LiveData<Int> get() = mutableLogFlag
    val user by lazy { User() }

    suspend fun checkUser(client: User, succeedText:String, wrongText:String, notFoundText:String) {
        val user = repository.getUser()
        for (data in user) {
            if (client.userId == data.userId && client.userPw == data.userPw) {
                mutableLogFlag.value = 1
                Toast.makeText(context.applicationContext, succeedText, Toast.LENGTH_SHORT).show()
                return
            } else if (client.userId == data.userId && client.userPw != data.userPw) {
                mutableLogFlag.value = 2
                Toast.makeText(context.applicationContext, wrongText, Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (logFlag.value == 0) {
            Toast.makeText(context.applicationContext, notFoundText, Toast.LENGTH_SHORT).show()
        }
    }
}