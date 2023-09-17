package com.jinproject.features.core

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.forEach
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.jinproject.features.core.listener.BottomNavigationController

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    open var bottomNavigationBarVisibility: Boolean = false
    open var topBarVisibility: Boolean = false
    private var bottomNavigationController: BottomNavigationController? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is BottomNavigationController) {
            bottomNavigationController = context
        }
    }

    abstract fun getViewDataBinding(): VB

    open fun initState() = Unit
    open fun subScribeUi() = Unit

    open fun initView() {
        showOrHideTopBar()
        showOrHideBottomNavigationBar()
        initState()
        subScribeUi()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewDataBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun showOrHideTopBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            if (topBarVisibility) show() else hide()
        }
    }

    private fun showOrHideBottomNavigationBar() {
        if (bottomNavigationBarVisibility)
            bottomNavigationController?.showBottomNavigation()
        else
            bottomNavigationController?.hideBottomNavigation()
    }

    fun setMenuColorOnDarkMode(menu: Menu) {
        when(AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_UNSPECIFIED -> {
                val typed = TypedValue()
                requireActivity().theme.resolveAttribute(R.attr.colorHeader, typed, true)
                menu.forEach { item ->
                    item.icon?.apply {
                        setTint(typed.data)
                        setTintMode(PorterDuff.Mode.SRC_ATOP)
                    }
                }
            }
            else -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}