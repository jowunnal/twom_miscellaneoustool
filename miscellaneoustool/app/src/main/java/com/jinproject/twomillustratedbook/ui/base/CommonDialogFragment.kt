package com.jinproject.twomillustratedbook.ui.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.jinproject.twomillustratedbook.databinding.DialogCommonBinding

class CommonDialogFragment private constructor(
    private val title: String,
    private val message: String? = "",
    private val positiveButtonText: String,
    private val negativeButtonText: String,
    private val listener: Listener
) : DialogFragment() {

    private lateinit var binding: DialogCommonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCommonBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner

        requireDialog().window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        tvHeader.text = title

        if (message != null) {
            if (message.isNotBlank())
                etPrice.setText(message)
        } else {
            etPrice.visibility = View.GONE
        }

        btnPositive.text = positiveButtonText
        btnPositive.setOnClickListener {
            listener.onPositiveButtonClick(etPrice.text.toString())
            dismissAllowingStateLoss()
        }
        btnNegative.text = negativeButtonText
        btnNegative.setOnClickListener {
            listener.onNegativeButtonClick()
            dismissAllowingStateLoss()
        }
    }

    abstract class Listener {
        open fun onNegativeButtonClick() = Unit
        abstract fun onPositiveButtonClick(value: String)
    }

    companion object {
        private const val TAG = "CommonDialogFragment"

        fun show(
            fragmentManager: FragmentManager,
            title: String,
            message: String?,
            positiveButtonText: String,
            negativeButtonText: String,
            listener: Listener
        ) {
            CommonDialogFragment(
                title = title,
                message = message,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                listener = listener
            ).show(fragmentManager, TAG)
        }
    }
}