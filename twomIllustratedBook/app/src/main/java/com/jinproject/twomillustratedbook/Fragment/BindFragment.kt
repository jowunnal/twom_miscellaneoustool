package com.jinproject.twomillustratedbook.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.jinproject.twomillustratedbook.utils.setBottomNavigationVisability

abstract class BindFragment<T:ViewDataBinding>(@LayoutRes private val layoutRes:Int,private val visability:Boolean):Fragment(){
    var _binding:T ?= null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater,layoutRes,container,false)
        setBottomNavigationVisability(requireActivity(),visability)
        return binding.root
    }

    override fun onDestroyView() {
        _binding=null
        super.onDestroyView()
    }

}