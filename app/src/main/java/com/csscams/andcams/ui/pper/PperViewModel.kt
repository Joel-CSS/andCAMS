package com.csscams.andcams.ui.pper

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.csscams.andcams.database.PayPeriod
import com.csscams.andcams.database.PayPeriods
import com.csscams.andcams.repository.PayPeriodRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val TAG = "PperViewModel"

class PperViewModel(application: Application) : AndroidViewModel(application){
    private val _text = MutableLiveData<String>().apply {
        value = "This is Pay Period Fragment"
    }
    val text: LiveData<String> = _text
    private var camsRepository = PayPeriodRepository(application)
    var payPeriods : MutableLiveData<PayPeriods> = MutableLiveData()

    fun getPayPeriods() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                camsRepository.getAllPayPeriods()
            }
            payPeriods = camsRepository.payPeriods
        }
    }
    //@Bindable
    //var payPeriods = MutableLiveData<PayPeriods>()

//    val currentPayPeriod = MutableLiveData<PayPeriod>().apply {
//        value = payPeriods.getCurrentPayPeriod()
//    }

    //val repository = CamsRepository(application)
    //var payPeriods = repository.getAllPayPeriods()

//    suspend fun getAllPayPeriods(){
//        withContext(Dispatchers.IO) {
//            payPeriods = repository.getAllPayPeriods()
//        }
//
//    }
//
    fun getNextPper(): PayPeriod?{
        return payPeriods.value?.getNextPayPeriod()
    }

    fun getPrevPper(): PayPeriod?{
        return payPeriods.value?.getPrevPayPeriod()
    }

    fun getCurrentPper(): PayPeriod?{
        return payPeriods.value?.getCurrentPayPeriod()
    }
}

