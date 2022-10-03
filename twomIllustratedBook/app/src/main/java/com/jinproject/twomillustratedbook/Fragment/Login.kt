package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.jinproject.twomillustratedbook.viewModel.LoginViewModel
import com.jinproject.twomillustratedbook.databinding.AlarmLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Login:Fragment() {
    var _binding:AlarmLoginBinding ?= null
    val binding get() = _binding!!
    val loginViewModel:LoginViewModel by viewModels()
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