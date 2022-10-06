package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.AlarmRegisterBinding
import com.jinproject.twomillustratedbook.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Register : BindFragment<AlarmRegisterBinding>(R.layout.alarm_register,false) {
    private val registerViewModel : RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registerViewModel=registerViewModel
        binding.lifecycleOwner=viewLifecycleOwner
        var checkErrorFlag=false
        binding.btnSignUP.setOnClickListener {
            if(binding.editTextName.text.isNullOrEmpty()|| binding.editTextPW.text.isNullOrEmpty() || binding.editTextPWCheck.text.isNullOrEmpty()){
                Toast.makeText(requireActivity(),"아이디,비밀번호,비밀번호 확인은 반드시 입력해주세요.",Toast.LENGTH_SHORT).show()
            }
            else if(binding.etName.helperText?.isNotEmpty() == true || binding.etAuthority.helperText?.isNotEmpty() == true ||
                binding.etPW.helperText?.isNotEmpty() == true || binding.etPWCheck.helperText?.isNotEmpty() == true) {
                Toast.makeText(requireActivity(),"올바른 형식으로 입력되지 않았습니다.",Toast.LENGTH_SHORT).show()
            }
            else if(binding.editTextPW.text.toString()!=binding.editTextPWCheck.text.toString()){
                Toast.makeText(requireActivity(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
            }
            else{
                checkErrorFlag=true
            }
        }
        registerViewModel.logFlag.observe(viewLifecycleOwner,Observer{
            if(it and checkErrorFlag){
                Navigation.findNavController(view).popBackStack()
                registerViewModel.mutableLogFlag.value=false
            }
        })

    }
}