package com.csscams.andcams.database

import org.json.JSONException
import org.json.JSONObject

open class County(var countyId: String = "", var name: String = "", var web_prefix: String = "") {

    fun loadFromJson(countyJSON: JSONObject): Boolean{
        try {
            countyId = countyJSON.getString("abbreviation")
            name = countyJSON.getString("name")
            web_prefix = countyJSON.getString("prefix")
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
        return true
    }
}
