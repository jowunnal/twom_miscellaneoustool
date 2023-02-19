package com.jinproject.twomillustratedbook.domain.Item

import androidx.databinding.ObservableField
import androidx.room.Entity
import androidx.room.PrimaryKey

data class RoomInfo(var userName:ObservableField<String>, var userPw:ObservableField<String>, var userAuthority:ObservableField<String>)

