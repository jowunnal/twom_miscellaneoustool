package com.jinproject.twomillustratedbook.ui.Fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.jinproject.twomillustratedbook.R
import com.jinproject.twomillustratedbook.databinding.AlarmLoginBinding
import com.jinproject.twomillustratedbook.databinding.AlarmRegisterBinding
import com.jinproject.twomillustratedbook.ui.base.BaseFragment
import com.jinproject.twomillustratedbook.ui.viewModel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Register : BaseFragment<AlarmRegisterBinding>() {
    private val registerViewModel : RegisterViewModel by viewModels()

    override fun getViewDataBinding(): AlarmRegisterBinding = AlarmRegisterBinding.inflate(layoutInflater)
    override var topBarVisibility: Boolean = true
    override var bottomNavigationBarVisibility: Boolean = true

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