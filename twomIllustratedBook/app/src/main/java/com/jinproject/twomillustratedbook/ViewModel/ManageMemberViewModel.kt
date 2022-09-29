package com.example.example_kakaologinapi.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.example_kakaologinapi.database.User
import com.example.example_kakaologinapi.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class ManageMemberViewModel @Inject constructor(private val app:Application, private val repository: UserRepositoryImpl) : ViewModel() {
    var mutableLogFlag = MutableLiveData(0)
    val logFlag: LiveData<Int> get() = mutableLogFlag
    val user by lazy { User("","","") }

    suspend fun checkUser(client: User, succeedText:String, wrongText:String, notFoundText:String) {
        val user = repository.getUser()
        for (data in user) {
            if (client.userId == data.userId && client.userPw == data.userPw) {
                mutableLogFlag.value = 1
                Toast.makeText(app.applicationContext, succeedText, Toast.LENGTH_SHORT).show()
                return
            } else if (client.userId == data.userId && client.userPw != data.userPw) {
                mutableLogFlag.value = 2
                Toast.makeText(app.applicationContext, wrongText, Toast.LENGTH_SHORT).show()
                return
            }
        }
        if (logFlag.value == 0) {
            Toast.makeText(app.applicationContext, notFoundText, Toast.LENGTH_SHORT).show()
        }
    }
}