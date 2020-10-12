package com.csscams.andcams.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.csscams.andcams.database.CamsCloud
import com.csscams.andcams.database.PayPeriods

class PayPeriodRepository(context: Context) {
    //TODO: val dao = CamsDatabase.getInstance(context).camsDAO

    var payPeriods = MutableLiveData <PayPeriods>()

    private fun getFromLocalDB(): Boolean{
        return false
    }

    fun getAllPayPeriods() {
        if (getFromLocalDB()) {
            //TODO: dao.getAllPayPeriods()
        }else{
            CamsCloud.getInstance(this).getAllPayPeriods(this)
        }

    }

/*TODO:
    suspend fun insertPayPeriod(period: PayPeriod){
        dao.insertPayPeriod(period)
    }

    suspend fun updatePayPeriod(period: PayPeriod){
        dao.updatePayPeriod(period)
    }

    suspend fun deletePayPeriod(period: PayPeriod){
        dao.deletePayPeriod(period)
    }

    suspend fun deleteAllPayPeriod(){
        dao.deleteAllPayPeriods()
    }*/
}
