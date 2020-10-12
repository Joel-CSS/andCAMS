package com.csscams.andcams.database

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Parcelize
class Timecard(var date: LocalDate, var tsk: String, var ear: String,
               var prlSrq: String, var rdm: String, var bgm: String, var vem: String,
               var repno: String, var repDescr: String, var eqpno: String,
               var tskDescr: String,var earDescr: String,
               var prlSrqDescr: String,var rdmDescr: String,var bgmDescr: String,
               var vemDescr: String,var eqpDescr: String,
               var regHrs: Double, var otHrs: Double, var specHrs: Double,
               var eqpUnits: Double, var accUnits: Double): Parcelable {
    @IgnoredOnParcel
    var id: Long = 0
    constructor() : this(
        LocalDate.MIN,"","","","","","",
    "","","","","","",
        "","","","",
        0.0,0.0,0.0,0.0,0.0)

    fun loadFromJson(timedata: JSONObject): Boolean{
        try {
            val datestr = timedata.getString("date").substringBefore(" ")
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH)
            date = LocalDate.parse(datestr, formatter)

            tsk = timedata.getString("tsk")
            tskDescr = timedata.getString("tsk_descr")
            ear = timedata.getString("ear")
            earDescr = timedata.getString("ear_descr")
            val prl = timedata.getString("prl")
            val prlDescr = timedata.getString("prl_descr")
            val srqDescr = timedata.getString("srq_descr")

            val srq = timedata.getString("srq")
            if (prl != ""){
                prlSrq = prl
                prlSrqDescr = prlDescr
            } else{
                prlSrq = srq
                prlSrqDescr = srqDescr
            }

            rdm = timedata.getString("rdm")
            rdmDescr = timedata.getString("rdm_descr")
            bgm = timedata.getString("bgm")
            bgmDescr = timedata.getString("bgm_descr")

            vem = timedata.getString("vem")
            vemDescr = timedata.getString("vem_descr")
            repno = timedata.getString("repno")
            repDescr  = timedata.getString("rep_descr")
            eqpno = timedata.getString("eqpno")
            eqpDescr = timedata.getString("eqp_descr")

            regHrs = timedata.getString("reg_hrs").toDouble()
            otHrs = timedata.getString("ot_hrs").toDouble()
            specHrs = timedata.getString("spec_hrs").toDouble()
            eqpUnits = timedata.getString("eqp_units").toDouble()
            accUnits = timedata.getString("acc_units").toDouble()
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
        return true
    }
}
