package com.jinproject.twomillustratedbook.viewModel
import android.content.Context
import androidx.lifecycle.*
import com.jinproject.twomillustratedbook.Repository.UserRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(@ApplicationContext private val context:Context, private val repository: UserRepositoryImpl)
    : ManageMemberViewModel(context, repository) {

    fun loginUser(){
        viewModelScope.launch(Dispatchers.Main){checkUser(user,"환영합니다.","비밀번호가 틀렸습니다.","존재하지 않는 계정 입니다.")}
    }
}