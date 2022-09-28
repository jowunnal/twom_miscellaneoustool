package com.jinproject.twomillustratedbook.ViewModel

class AlarmPresenter {
    fun getMonsterCode(monsName:String):Int{
        var monsCode=0
        for(i in monsName.toCharArray()){
            monsCode+=i.toInt()
        }
        return monsCode
    }
}