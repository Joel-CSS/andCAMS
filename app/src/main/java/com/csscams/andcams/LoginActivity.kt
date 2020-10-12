package com.csscams.andcams

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.csscams.andcams.globals.CurrentCounty
import com.csscams.andcams.globals.CurrentUser
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONArray
import org.json.JSONException

private const val TAG = "LoginActivity"

enum class ClientUserTask {
    LOAD_CLIENT, LOAD_USER, GET_USER_INFO, NONE
}

class LoginActivity : AppCompatActivity(), GetCssData.OnDownloadComplete {

    private var fClientUserTask = ClientUserTask.NONE

    private fun loginFailed(m: String){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(m)
            .setCancelable(false)
            .setPositiveButton("OK") { _, _ ->
                //finish()
            }

        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle("Login Failed")
        // show alert dialog
        alert.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        /*get client from preferences*/
        val prefs =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val tmpClient = prefs.getString(getString(R.string.prefs_client_id), null)

        if (tmpClient != null) {
            editClientCode.setText(tmpClient)
            val params = mapOf("client" to tmpClient)
            if (editClientCode.text.toString() != ""){
                fClientUserTask = ClientUserTask.LOAD_CLIENT
                val cssData = GetCssData(this, METHOD.GET, params)
                cssData.execute(cssData.getURL("getCountyInfo"))
            }
        }

        logInBtn.setOnClickListener {
            if (CurrentCounty.countyId == ""){
                if(editClientCode.text.toString() != ""){
                    val params = mapOf("client" to editClientCode.text.toString())
                    fClientUserTask = ClientUserTask.LOAD_CLIENT
                    val cssData = GetCssData(this, METHOD.GET, params)
                    cssData.execute(cssData.getURL("getCountyInfo"))
                }else{
                    loginFailed("Client cannot be blank")
                }
             }
            else if ((editTextTextPersonName.text.toString().trim()!="")&&
                (editTextPassword.text.toString().trim()!="")){
                val params = mapOf(
                    "client" to editClientCode.text.toString(),
                    "username" to editTextTextPersonName.text.toString(),
                    "password" to editTextPassword.text.toString()
                )

                fClientUserTask = ClientUserTask.LOAD_USER
                val cssData = GetCssData(this, METHOD.GET, params)
                cssData.execute(cssData.getURL("getUser"))
            }else{
                loginFailed("User Name and Password cannot be blank")
            }
        }
    }

    private fun getUserInfo(userId: String){
        val params = mapOf("client" to editClientCode.text.toString(),
            "userid" to userId)

        fClientUserTask = ClientUserTask.GET_USER_INFO
        val cssData = GetCssData(this, METHOD.GET, params)
        cssData.execute(cssData.getURL("getUserInfo"))
    }

    private fun saveCountyCode(countyId: String){
        val prefs =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with (prefs.edit()) {
            putString(getString(R.string.prefs_client_id), countyId)
            commit()
        }
    }

    private fun saveUserInfo(userId: String){
        val prefs =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with (prefs.edit()) {
            putString(getString(R.string.prefs_user_id), userId)
            commit()
        }
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "OnDownloadComplete: $data")
            when (fClientUserTask) {
                ClientUserTask.LOAD_CLIENT -> {
                    try {
                        val countyJSON = JSONArray(data.trim())
                        CurrentCounty.loadFromJson(countyJSON.getJSONObject(0))
                        saveCountyCode(CurrentCounty.countyId)
                        if (CurrentCounty.countyId != "") {
                            val prefs =
                                this.getSharedPreferences(
                                    getString(R.string.preference_file_key),
                                    Context.MODE_PRIVATE
                                )
                            val tmpUserId = prefs.getString(getString(R.string.prefs_user_id), null)
                            if ((tmpUserId != null) && (tmpUserId != "")) {
                                getUserInfo(tmpUserId)
                            } else {
                                if ((editClientCode.text.toString().trim() != "") &&
                                    (editTextPassword.text.toString().trim() != "")) {
                                    val params = mapOf(
                                        "client" to editClientCode.text.toString(),
                                        "username" to editTextTextPersonName.text.toString(),
                                        "password" to editTextPassword.text.toString()
                                    )

                                    fClientUserTask = ClientUserTask.LOAD_USER
                                    val cssData = GetCssData(this, METHOD.GET, params)
                                    cssData.execute(cssData.getURL("getUser"))
                                }
                            }

                        }

                    }catch (e: JSONException){
                        e.printStackTrace()
                        Log.e(TAG, "Error parsing County JSON")
                    }

                }
                ClientUserTask.LOAD_USER -> {
                    try {
                        val userJSON = JSONArray(data.trim())
                        CurrentUser.loadFromJson(userJSON.getJSONObject(0))
                        saveUserInfo(editTextTextPersonName.text.toString())
                        val intent = Intent(this, AppsActivity::class.java)
                        startActivity(intent)
                    }catch (e: JSONException){
                        e.printStackTrace()
                        loginFailed(data)
                    }
                }
                ClientUserTask.GET_USER_INFO -> {
                    try {
                        val userJSON = JSONArray(data.trim())
                        CurrentUser.loadFromJson(userJSON.getJSONObject(0))
                        //saveUserInfo(editTextTextPersonName.text.toString())
                        val intent = Intent(this, AppsActivity::class.java)
                        startActivity(intent)
                    }catch (e: JSONException){
                        e.printStackTrace()
                        Log.e(TAG, "Error parsing User JSON")
                    }
                }
                else ->{
                    Log.d(TAG, "No Task involved")
                }
            }
        } else {
            Log.d(TAG, "OnDownloadComplete failed: $status.  Error: $data")
            loginFailed("Error Logging In")
        }
    }
}
