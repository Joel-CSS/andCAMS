package com.csscams.andcams.database

import com.csscams.andcams.SingletonHolder
import com.csscams.andcams.repository.PayPeriodRepository

//object CamsCloud: CamsServer()
class CamsCloud private constructor(payPeriodRepository: PayPeriodRepository): CamsServer() {

    companion object : SingletonHolder<CamsCloud, PayPeriodRepository>(::CamsCloud)
}
