package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.viewModel.LoginViewModel
import com.jinproject.twomillustratedbook.databinding.AlarmLoginBinding
import com.jinproject.twomillustratedbook.databinding.AlarmRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login : BindFragment<AlarmLoginBinding>(R.layout.alarm_login,false) {
    val loginViewModel:LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginViewModel=loginViewModel
        binding.navigation = Navigation.findNavController(view)

        loginViewModel.logFlag.observe(viewLifecycleOwner, Observer {
            if(it==1){
                Navigation.findNavController(view).popBackStack()
                loginViewModel.mutableLogFlag.value=0
            }
        })
    }
}