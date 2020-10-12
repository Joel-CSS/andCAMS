package com.csscams.andcams.database

import org.json.JSONException
import org.json.JSONObject

open class User(var userId: String, var name: String, var empNo: String) {
    constructor() : this("","","")
    fun loadFromJson(userJSON: JSONObject): Boolean{
        try {
            userId = userJSON.getString("userid")
            name = userJSON.getString("FirstName") + " " + userJSON.getString("LastName")
            empNo = userJSON.getString("Empno")
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
        return true
    }
}