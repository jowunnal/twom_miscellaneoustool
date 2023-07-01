package com.miscellaneoustool.app.ui.base

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.miscellaneoustool.app.R

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    open var bottomNavigationBarVisibility: Boolean = false
    open var topBarVisibility: Boolean = false

    abstract fun getViewDataBinding(): VB

    open fun initState() = Unit
    open fun subScribeUi() = Unit

    open fun initView() {
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

    override fun onResume() {
        super.onResume()

        showOrHideTopBar(topBarVisibility)

        requireActivity()
            .findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .visibility = if (bottomNavigationBarVisibility) View.VISIBLE else View.GONE
    }

    fun showOrHideTopBar(visibility: Boolean) {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            if (visibility) show() else hide()
        }
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
        _binding = null
        super.onDestroyView()
    }

}