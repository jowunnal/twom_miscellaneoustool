package com.jinproject.twomillustratedbook.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.firebase.database.FirebaseDatabase
import com.jinproject.twomillustratedbook.Item.Room
import com.jinproject.twomillustratedbook.Item.TimerItem
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.ViewModel.LoginViewModel
import com.jinproject.twomillustratedbook.databinding.AlarmLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login:Fragment() {
    var _binding:AlarmLoginBinding ?= null
    val binding get() = _binding!!
    lateinit var loginViewModel:LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=AlarmLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.logFlag.observe(viewLifecycleOwner, Observer {
            if(it==1){
                Navigation.findNavController(view).popBackStack()
                loginViewModel.mutableLogFlag.value=0
            }
        })
    }
}