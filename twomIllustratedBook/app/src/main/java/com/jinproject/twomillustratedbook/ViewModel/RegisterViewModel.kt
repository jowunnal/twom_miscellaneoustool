package com.example.example_kakaologinapi.viewModel

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import com.example.example_kakaologinapi.database.User
import com.example.example_kakaologinapi.item.RegisterInfo
import com.example.example_kakaologinapi.repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val app:Application, private val repository: UserRepositoryImpl) :
    ManageMemberViewModel(app, repository) {
    val info by lazy{ RegisterInfo(ObservableField(""),ObservableField(""),ObservableField(""),ObservableField("")) }

    suspend fun registerUser():Boolean{
        return withContext(viewModelScope.coroutineContext + Dispatchers.Main) {
            var result = false
            checkUser(
                user,
                "이미 존재하는 아이디 입니다.",
                "이미 존재하는 아이디 입니다.",
                user.userName + "님의 회원가입이 완료되었습니다."
            )
            if (logFlag.value == 0) {
                repository.addUser(user)
                result = true
                Log.d("test", "pres: ${user.userId} ${user.userPw}")
            }
            mutableLogFlag.value = 0
            result
        }
    }

    fun deleteUser(id:String,pw:String){
        viewModelScope.launch(Dispatchers.IO) { repository.deleteUser(User(id,"jinho",pw)) }
    }

    private fun checkRegisterInfoIsCorrect(inputString: String, pattern:String, text:String):String{
        var txt=""
        if(inputString.contains(pattern.toRegex())){
            txt = text
        }
        return txt
    }

    val checkNameIsCorrect = fun(inputString:String){
        info.r_Name.set(checkRegisterInfoIsCorrect(inputString,"[^0-9a-zA-Zㄱ-힣_]","특수문자는 (_)를 제외하고 입력될 수 없습니다."))
    }

    val checkIdIsCorrect = fun(inputString:String){
        info.r_Id.set(checkRegisterInfoIsCorrect(inputString,"[^0-9a-zA-Zㄱ-힣]","특수문자는 입력될수 없습니다."))
    }

    val checkPwIsCorrect = fun(inputString: String){
        info.r_Pw.set(checkRegisterInfoIsCorrect(inputString,"[^0-9a-zA-Zㄱ-힣!@#*]","영대소문자,숫자,특수문자(!,@,#,*)만 가능합니다."))

    }

    val checkPwDoubleIsCorrect = fun(inputString: String){
        if(inputString!=user.userPw){
            info.r_PwDouble.set("비밀번호가 일치하지 않습니다.")
            Log.d("test",user.userPw+" / "+inputString)
        }
        else
            info.r_PwDouble.set("")
    }
}