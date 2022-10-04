package com.jinproject.twomillustratedbook.utils

import android.app.Activity
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jinproject.twomillustratedbook.R

fun setBottomNavigationVisability(activity:Activity,bool:Boolean){
    if(bool)
        activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility=
            View.VISIBLE
    else
        activity.findViewById<BottomNavigationView>(R.id.bottomNavigationView).visibility=
            View.GONE
}