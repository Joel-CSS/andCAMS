package com.csscams.andcams

import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.csscams.andcams.globals.CurrentCounty
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class METHOD {
    POST, GET, DELETE
}

enum class DownloadStatus{
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

private const val TAG = "GetCssData"

class GetCssData(private val listener: OnDownloadComplete, private val method: METHOD,
                 params: Map<String, String>? = null
): AsyncTask<String, Void, String>() {

    private val CSS_KEY = "f0743722bda1b3df9b21565d52dd9e07b4820c91"

    private val CSS_URL = "https://www.win-cams.com"

    private var downloadStatus: DownloadStatus = DownloadStatus.IDLE
    private var fparams = mutableMapOf<String, String>()

    init{
        if (params != null) {
            for ((k,v) in params){
                fparams[k] = v
            }
        }

        fparams["key"] = CSS_KEY
        if (CurrentCounty.countyId != "") {
            fparams["client"] = CurrentCounty.countyId
        }
    }

    interface OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }

    fun getURL(request:String):String{
        var resultURl: String = CSS_URL
        if (CurrentCounty.web_prefix != "") {
            resultURl += "/" + CurrentCounty.web_prefix
        }
        resultURl += "/apis/"+request+".php"
        var uri = Uri.parse(resultURl)
        var builder = uri.buildUpon()
        for ((key,value) in fparams){
            builder = builder.appendQueryParameter(key,value)
        }
        uri = builder.build()
        Log.d(TAG,"onPostExecute called with parameter "+uri.toString())

        return uri.toString()
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG,"onPostExecute called with parameter $result")
        listener.onDownloadComplete(result, downloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null){
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return "No URL Specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (e: Exception){
            val errorMessage = when(e){
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "do In Background: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "do In Background: Error reading data ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "do In Background: Permission denied ${e.message}"
                }else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "do In Background: Unknown Error ${e.message}"
                }
            }
            Log.e(TAG, errorMessage)

            return errorMessage
        }
    }
}
