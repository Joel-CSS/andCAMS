package com.csscams.andcams.ui.weekly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeeklyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Weekly Fragment"
    }
    val text: LiveData<String> = _text
}