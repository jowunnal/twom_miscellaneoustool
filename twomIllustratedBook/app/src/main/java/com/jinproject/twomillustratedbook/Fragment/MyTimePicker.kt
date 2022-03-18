package com.jinproject.twomillustratedbook.Fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.jinproject.twomillustratedbook.R
import java.text.SimpleDateFormat
import java.util.*

class MyTimePicker : DialogFragment() {
    private lateinit var myView:View
    private lateinit var hour:NumberPicker
    private lateinit var min:NumberPicker
    private lateinit var sec:NumberPicker
    private var setTime:(hour:Int,min:Int,sec:Int) -> Unit={_,_,_->}
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder=AlertDialog.Builder(it)
            myView=requireActivity().layoutInflater.inflate(R.layout.alarm_custom_dialog,null)
            hour=myView.findViewById<NumberPicker>(R.id.cd_picker_hour)
            hour.maxValue=23
            hour.minValue=0
            val getTime= SimpleDateFormat("HH:mm:ss").format(Date(System.currentTimeMillis()))
            hour.value=Integer.parseInt(getTime.split(":")[0])
            min=myView.findViewById<NumberPicker>(R.id.cd_picker_min)
            min.maxValue=59
            min.minValue=0
            min.value=Integer.parseInt(getTime.split(":")[1])
            sec=myView.findViewById<NumberPicker>(R.id.cd_picker_sec)
            sec.maxValue=59
            sec.minValue=0
            sec.value=Integer.parseInt(getTime.split(":")[2])
            builder.setView(myView)
            builder.setTitle("타이머설정")
            builder.setPositiveButton("설정하기") { _, _ ->
                setTime(hour.value,min.value,sec.value)
            }
            builder.setNegativeButton("취소"){ _, _ ->
            }
            builder.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }

    fun setOnTimeListener(setTime:(hour:Int,min:Int,sec:Int) -> Unit){
        this.setTime=setTime
    }

}