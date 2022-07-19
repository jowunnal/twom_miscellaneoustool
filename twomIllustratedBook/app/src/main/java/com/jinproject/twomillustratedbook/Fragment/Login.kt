package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.jinproject.twomillustratedbook.Item.Room
import com.jinproject.twomillustratedbook.Item.User
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
        val room= Room("","")

        binding.buttonLogin.setOnClickListener {
            db.child("RoomList").get().addOnSuccessListener {
                for(data in it.children.iterator()){
                    if(binding.etId==data.child("roomId").value){
                        room.roomId=binding.etId.text.toString()
                        room.roomPw=binding.etPw.text.toString()
                    }
                }
            }
        }

        binding.buttonSign.setOnClickListener {
            db.child("RoomList").get().addOnSuccessListener {
                var flagExist=false
                for(data in it.children.iterator()){
                    if(binding.etId==data.child("roomId").value){
                        continue
                    }
                    else{
                        flagExist=true
                    }
                }
                if(flagExist){
                    db.child("RoomList").setValue(Room(binding.etId.text.toString(),binding.etPw.text.toString()))
                }else{
                    Toast.makeText(requireActivity(),"이미 존재하는 아이디 입니다. 다시입력해주세요.",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}