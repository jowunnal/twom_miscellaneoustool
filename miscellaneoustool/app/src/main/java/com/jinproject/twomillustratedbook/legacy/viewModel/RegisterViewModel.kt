package com.jinproject.twomillustratedbook.legacy.viewModel
/*
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import com.jinproject.twomillustratedbook.domain.Item.RegisterInfo
import com.jinproject.twomillustratedbook.domain.Item.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(@ApplicationContext private val context:Context) :
    ManageMemberViewModel(context) {
    val info by lazy{ RegisterInfo(ObservableField(""),ObservableField(""),ObservableField(""),ObservableField("")) }

    fun registerUser(){
        db.child("RoomList").get().addOnSuccessListener {
            var flagExist = true
            for (data in it.children) {
                if (roomInfo.userName == data.child("roomId").value) {
                    flagExist = false
                    continue
                } else {
                    flagExist = true
                }
            }
            if (flagExist) {
                val key = db.child("RoomList").push().key
                db.child("RoomList").child(key!!).setValue(
                    Room(
                        roomInfo.userName.get()!!,
                        roomInfo.userPw.get()!!,
                        null,
                        roomInfo.userAuthority.get()!!
                    )
                )
                mutableLogFlag.value=true
                Toast.makeText(context, "등록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "이미 존재하는 아이디 입니다. 다시입력해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkRegisterInfoIsCorrect(inputString: String, pattern:String, text:String):String{
        var txt=""
        if(inputString.contains(pattern.toRegex())){
            txt = text
        }
        return txt
    }

    val checkIdIsCorrect = fun(inputString:String){
        info.r_Name.set(checkRegisterInfoIsCorrect(inputString,"[^0-9a-zA-Zㄱ-힣]","특수문자는 입력될수 없습니다."))
    }

    val checkPwIsCorrect = fun(inputString: String){
        info.r_Pw.set(checkRegisterInfoIsCorrect(inputString,"[^0-9a-zA-Zㄱ-힣!@#*]","영대소문자,숫자,특수문자(!,@,#,*)만 가능합니다."))
    }

    val checkAuthorityIsCorrect = fun(inputString:String){
        if(inputString.isNotEmpty()){
            info.r_Pw.set(checkRegisterInfoIsCorrect(inputString,"[^0-9a-zA-Zㄱ-힣!@#*]","영대소문자,숫자,특수문자(!,@,#,*)만 가능합니다."))
        }
    }

    val checkPwDoubleIsCorrect = fun(inputString: String){
        if(inputString!=roomInfo.userPw.get()){
            info.r_PwDouble.set("비밀번호가 일치하지 않습니다.")
            Log.d("test",roomInfo.userPw.get()+" / "+inputString)
        }
        else
            info.r_PwDouble.set("")
    }
}
 */