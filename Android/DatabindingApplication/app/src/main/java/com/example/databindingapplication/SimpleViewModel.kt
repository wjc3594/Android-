package com.example.databindingapplication

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SimpleViewModel: ViewModel() {
    var name=MutableLiveData<String>().apply {
        value="nihao"
    }

    fun change(){
        name.value="fdasjflasjfajfjsalfjlsajflsajflsajfl"
    }
}