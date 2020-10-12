package com.csscams.andcams.ui.pper

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.csscams.andcams.*
import com.csscams.andcams.database.PayPeriod
import com.csscams.andcams.database.PayPeriods
import com.csscams.andcams.database.Timecard
import com.csscams.andcams.globals.CurrentUser
import kotlinx.android.synthetic.main.fragment_pper.*
import org.json.JSONArray
import org.json.JSONException

private const val TAG = "PperFrag"

enum class PperTask {LOAD_ALL_PPER, LOAD_TIMECARD, NONE}

class PperFragment : Fragment(), GetCssData.OnDownloadComplete  {

    private var fTask = PperTask.NONE

    private lateinit var pperViewModel: PperViewModel
    private lateinit var layout: ConstraintLayout

    private val adapter = PperRecyclerViewAdapter(ArrayList())

    var payPeriods = PayPeriods()
    var timecards = mutableListOf<Timecard>()

    override fun onResume() {
        super.onResume()

        val per = payPeriods.getCurrentPayPeriod()
        if (per != null) {
            (activity as? AppCompatActivity)?.supportActionBar?.title = "PayPeriod Timecard: "+per.pper
            (activity as? AppCompatActivity)?.supportActionBar?.subtitle = per.startDate+"~"+per.endDate
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        /*TODO: I have spent way too much time struggling with the View Model pattern.
        For now stick with what I know.*/
        pperViewModel =
            ViewModelProviders.of(this).get(PperViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_pper, container, false)

        //pperViewModel.getPayPeriods()
        getPayPeriods()
        layout = root.findViewById(R.id.constraint_layout)

        layout.setOnTouchListener(object : OnSwipeTouchListener(activity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                val per = payPeriods.getNextPayPeriod()
                if (per != null) {
                    Toast.makeText(
                        activity,
                        "Swipe Left: Pay Period " + per.pper,
                        Toast.LENGTH_SHORT
                    ).show()
                    setPayPeriodLabels(per)
                } else {
                    Toast.makeText(
                        activity,
                        "Swipe Left: No Pay Period found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                val per = payPeriods.getPrevPayPeriod()
                if (per != null) {
                    Toast.makeText(
                        activity,
                        "Swipe Right: Pay Period " + per.pper,
                        Toast.LENGTH_SHORT
                    ).show()
                    setPayPeriodLabels(per)
                } else {
                    Toast.makeText(
                        activity,
                        "Swipe Right: No Pay Period found",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

        })

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        time_recycler_view.layoutManager = LinearLayoutManager(activity)
        time_recycler_view.adapter = adapter
    }
    
    private fun getPayPeriods() {
        fTask = PperTask.LOAD_ALL_PPER
        val cssData = GetCssData(this, METHOD.GET)
        cssData.execute(cssData.getURL("getAllPper"))
    }

    fun getTimecards(startDate: String, endDate: String){
        fTask = PperTask.LOAD_TIMECARD
        val params = mapOf(
            "empno" to CurrentUser.empNo,
            "start_date" to startDate,
            "end_date" to endDate
        )
        fTask = PperTask.LOAD_TIMECARD
        val cssData = GetCssData(this, METHOD.GET, params)
        cssData.execute(cssData.getURL("getTimeFromRange"))
    }

    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        Log.d(TAG,"entering download complete")
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "OnDownloadComplete: $data")
            when (fTask) {
                PperTask.LOAD_ALL_PPER -> {
                    try {
                        payPeriods.loadFromJson(data)
                        val currentPper = payPeriods.getCurrentPayPeriod()
                        if (currentPper != null) {
                            setPayPeriodLabels(currentPper)
                            getTimecards(currentPper.startDate, currentPper.endDate)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e(TAG, "Error parsing Pay Period JSON")
                    }

                }
                PperTask.LOAD_TIMECARD -> {
                    try {
                        loadTime(data)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e(TAG, "Error parsing Pay Period JSON")
                    }
                }
                else ->{
                    Log.d(TAG, "No Task involved")
                }
            }
        } else {
            Log.d(TAG, "OnDownloadComplete failed: $status.  Error: $data")
        }
    }

    private fun loadTime(t: String){
        val jsonData = t.trim()

        try {
            val jsonItems = JSONArray(jsonData)
            for (i in 0 until jsonItems.length()){
                val item = jsonItems.getJSONObject(i)
                val timeObject = Timecard()
                timeObject.loadFromJson(item)
                timecards.add(timeObject)
            }
            if (timecards.count() > 0){
                adapter.loadNewData(timecards)
                time_recycler_view.visibility = View.VISIBLE
                text_pper.visibility = View.GONE
            }else{
                time_recycler_view.visibility = View.GONE
                text_pper.visibility = View.VISIBLE
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

    private fun setPayPeriodLabels(p: PayPeriod){
        val startDate = p.startDate.substring(5, 7) + "/" +
                p.startDate.substring(8)+ "/" +
                p.startDate.substring(0, 4)
        val endDate = p.endDate.substring(5, 7) + "/" +
                p.endDate.substring(8)+ "/" +
                p.endDate.substring(0, 4)
        (activity as? AppCompatActivity)?.supportActionBar?.title =
            "PayPeriod Timecard: " + p.pper


        (activity as? AppCompatActivity)?.supportActionBar?.subtitle =
            startDate + "~" + endDate

        //text_pper.setText("No Timecards for " + currentPper.pper)
        text_pper.setText(getString(R.string.no_timecards_for_per, p.pper))
        text_pper.visibility = View.VISIBLE
    }


}

