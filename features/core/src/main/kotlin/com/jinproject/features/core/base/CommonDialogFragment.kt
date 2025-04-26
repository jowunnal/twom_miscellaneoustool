package com.jinproject.features.core.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.jinproject.features.core.databinding.DialogCommonBinding
import androidx.core.graphics.drawable.toDrawable

class CommonDialogFragment private constructor(
    private val title: String,
    private val message: String? = "",
    private val positiveButtonText: String,
    private val negativeButtonText: String,
    private val listener: Listener,
    private val enabled: Boolean = true,
) : DialogFragment() {

    private lateinit var binding: DialogCommonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogCommonBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner

        requireDialog().window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
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

        isCancelable = enabled

        if(positiveButtonText.isBlank())
            btnPositive.visibility = View.GONE
        else {
            btnPositive.text = positiveButtonText
            btnPositive.setOnClickListener {
                listener.onPositiveButtonClick(etPrice.text.toString())
                if(enabled)
                    dismissAllowingStateLoss()
            }
        }

        if(negativeButtonText.isBlank())
            btnNegative.visibility = View.GONE
        else {
            btnNegative.text = negativeButtonText
            btnNegative.setOnClickListener {
                listener.onNegativeButtonClick()
                if(enabled)
                    dismissAllowingStateLoss()
            }
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
            listener: Listener,
            enabled: Boolean = true,
        ) {
            CommonDialogFragment(
                title = title,
                message = message,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                listener = listener,
                enabled = enabled,
            ).show(fragmentManager, TAG)
        }
    }
}