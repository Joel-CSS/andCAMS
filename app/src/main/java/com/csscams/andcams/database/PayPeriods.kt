package com.csscams.andcams.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG = "Pay Periods"

class PayPeriods {
    private val payPeriods = ArrayList<PayPeriod>()

    private var currentPayPeriod: PayPeriod? = null

    public fun add(p: PayPeriod){
        payPeriods.add(p)
    }

    public fun clear(){
        payPeriods.clear()
    }

    fun getPayPeriod(d: LocalDateTime): PayPeriod?{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val dateStr = d.format(formatter)
        for (p in payPeriods){
            if ((dateStr >= p.startDate) && (dateStr <= p.endDate)){
                return p
            }
        }
        return null
    }

    fun getCurrentPayPeriod(): PayPeriod?{
        if (currentPayPeriod == null){
            currentPayPeriod = getPayPeriod(LocalDateTime.now())
        }

        return currentPayPeriod
    }

    fun getPrevPayPeriod(): PayPeriod?{
        val ndx = payPeriods.indexOf(currentPayPeriod)
        if (ndx < payPeriods.size) {
            currentPayPeriod = payPeriods[ndx + 1]

        }
        return currentPayPeriod
    }

    fun getNextPayPeriod(): PayPeriod?{
        val ndx = payPeriods.indexOf(currentPayPeriod)
        if (ndx > 0) {
            currentPayPeriod = payPeriods[ndx - 1]

        }
        return currentPayPeriod
    }

    fun loadFromJson(data: String){
        val jsonData = data.trim()

        try {
            val jsonItems = JSONArray(jsonData)
            for (i in 0 until jsonItems.length()){
                val item = jsonItems.getJSONObject(i)
                val pperObject = PayPeriod()
                pperObject.loadFromJson(item)
                add(pperObject)

            }
        }catch (e: Exception){
            when(e) {
                is JSONException -> {
                    TAG +"-- getPayPeriod -- "+e.message
                    e.printStackTrace()
                }
                else -> {
                    TAG +"-- getPayPeriod -- "+e.message
                    e.printStackTrace()
                }
            }
        }
    }
}

@Entity
data class PayPeriod(
    @PrimaryKey
    var startDate: String,
    var endDate:String,
    var pper: String,
    var mon: String,
    var fiscYear: String)  {
    constructor() : this("","","","","")
    fun loadFromJson(userJSON: JSONObject): Boolean{
        try {
            startDate = userJSON.getString("start_date").substringBefore(" ")

            endDate = userJSON.getString("end_date").substringBefore(" ")

            pper = userJSON.getString("per")
            mon = userJSON.getString("mon")
            fiscYear = userJSON.getString("fisc_year")
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
        return true
    }

}
