package com.jinproject.twomillustratedbook.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.database.FirebaseDatabase
import com.jinproject.twomillustratedbook.Item.Room
import com.jinproject.twomillustratedbook.Item.TimerItem
import com.jinproject.twomillustratedbook.databinding.AlarmLoginBinding

class Login:Fragment() {
    var _binding:AlarmLoginBinding ?= null
    val binding get() = _binding!!
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

        val db=FirebaseDatabase.getInstance().reference

        val loginPreference=requireActivity().getSharedPreferences("login",Context.MODE_PRIVATE)
        var logFlag=false
        binding.buttonLogin.setOnClickListener {
            db.child("RoomList").get().addOnSuccessListener {
                for(data in it.children){
                    if(binding.etId.text.toString()==data.child("roomId").value && binding.etPw.text.toString()==data.child("roomPw").value){
                        loginPreference.edit().putString("id",binding.etId.text.toString()).apply()
                        loginPreference.edit().putString("pw",binding.etPw.text.toString()).apply()
                        loginPreference.edit().putString("key",data.key).apply()
                        loginPreference.edit().putString("authorityCode",binding.etCode.text.toString()).apply()
                        Toast.makeText(requireActivity(),"로그인 완료",Toast.LENGTH_LONG).show()
                        logFlag=true
                        Navigation.findNavController(view).popBackStack()
                        continue
                    }
                }
                if(!logFlag){
                Toast.makeText(requireActivity(),"아이디또는 패스워드가 일치하지 않습니다.",Toast.LENGTH_LONG).show() }
            }
        }

        binding.buttonSign.setOnClickListener {
            db.child("RoomList").get().addOnSuccessListener {
                var flagExist=true
                for(data in it.children){
                    if(binding.etId.text.toString()==data.child("roomId").value){
                        flagExist=false

                        continue
                    }
                    else{
                        flagExist=true
                    }
                }
                if(flagExist){
                    val key=db.child("RoomList").push().key
                    db.child("RoomList").child(key!!).setValue(Room(binding.etId.text.toString(),binding.etPw.text.toString(),null,binding.etCode.text.toString()))
                    Toast.makeText(requireActivity(),"등록이 완료되었습니다. 로그인버튼을 눌러주세요",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireActivity(),"이미 존재하는 아이디 입니다. 다시입력해주세요.",Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.buttonBack.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }
    }
}