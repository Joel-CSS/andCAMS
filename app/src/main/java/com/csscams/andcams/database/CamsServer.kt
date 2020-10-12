package com.csscams.andcams.database

import android.util.Log
import com.csscams.andcams.DownloadStatus
import com.csscams.andcams.GetCssData
import com.csscams.andcams.METHOD
import com.csscams.andcams.repository.PayPeriodRepository
import org.json.JSONArray
import org.json.JSONException

private const val TAG = "Cams Server"
enum class ServerTask {
    LOAD_PAYPERIODS, NONE
}

open class CamsServer() : GetCssData.OnDownloadComplete{
    var task = ServerTask.NONE
    var pperRepo: PayPeriodRepository? = null

    fun getAllPayPeriods(repo: PayPeriodRepository){
        if (pperRepo == null){
            pperRepo = repo
        }
        task = ServerTask.LOAD_PAYPERIODS

        val cssData = GetCssData(this, METHOD.GET)
        cssData.execute(cssData.getURL("getAllPper"))
    }

    private fun getPayPeriods(data: String){

        val jsonData = data.trim()

        try {
            val jsonItems = JSONArray(jsonData)
            for (i in 0 until jsonItems.length()){
                val item = jsonItems.getJSONObject(i)
                val pperObject = PayPeriod()
                pperObject.loadFromJson(item)
                pperRepo?.payPeriods?.value?.add(pperObject)

            }

            task = ServerTask.NONE
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

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            if (task == ServerTask.LOAD_PAYPERIODS) {
                Log.d(TAG, "OnDownloadComplete: $data")
                getPayPeriods(data)
            }
        }
    }
}

