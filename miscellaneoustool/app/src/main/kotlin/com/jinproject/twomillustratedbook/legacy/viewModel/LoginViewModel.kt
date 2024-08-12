package com.jinproject.twomillustratedbook.legacy.viewModel

/*
@HiltViewModel
class LoginViewModel @Inject constructor(@ApplicationContext private val context:Context)
    : ManageMemberViewModel(context) {

    fun loginUser(){
        db.child("RoomList").get().addOnSuccessListener {
            for (data in it.children) {
                if (roomInfo.userName.get() == data.child("roomId").value && roomInfo.userPw.get() == data.child(
                        "roomPw"
                    ).value
                ) {
                    loginPreference.edit().putString("id", roomInfo.userName.get()).apply()
                    loginPreference.edit().putString("pw", roomInfo.userPw.get()).apply()
                    loginPreference.edit().putString("key", data.key).apply()
                    loginPreference.edit()
                        .putString("authorityCode", roomInfo.userAuthority.get()).apply()
                    mutableLogFlag.value=true
                    Toast.makeText(context, "로그인 완료", Toast.LENGTH_LONG).show()
                    continue
                }
            }
            if (!logFlag.value!!) {
                Toast.makeText(context, "아이디또는 패스워드가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }
}*/