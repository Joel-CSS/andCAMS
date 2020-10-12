package com.csscams.andcams

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.csscams.andcams.database.Timecard
import java.time.format.DateTimeFormatter

class PperViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var dateEd: TextView = view.findViewById(R.id.dateEd)
    var tskEd: TextView = view.findViewById(R.id.tskEd)
    var tskDescr: TextView = view.findViewById(R.id.tskDescr)
    var earEd: TextView = view.findViewById(R.id.earEd)
    var earDescr: TextView = view.findViewById(R.id.earDescr)
    var prlsrqLbl: TextView = view.findViewById(R.id.prlsrqLbl)
    var prlsrqEd: TextView = view.findViewById(R.id.prlsrqEd)
    var prlsrqDescr: TextView = view.findViewById(R.id.prlsrqDescr)
    var rdmLbl: TextView = view.findViewById(R.id.rdmLbl)
    var rdmEd: TextView = view.findViewById(R.id.rdmEd)
    var rdmDescr: TextView = view.findViewById(R.id.rdmDescr)
    var bgmLbl: TextView = view.findViewById(R.id.bgmLbl)
    var bgmEd: TextView = view.findViewById(R.id.bgmEd)
    var bgmDescr: TextView = view.findViewById(R.id.bgmDescr)
    var vemLbl: TextView = view.findViewById(R.id.vemLbl)
    var vemEd: TextView = view.findViewById(R.id.vemEd)
    var vemDescr: TextView = view.findViewById(R.id.vemDescr)
    var repLbl: TextView = view.findViewById(R.id.rdmLbl)
    var repEd: TextView = view.findViewById(R.id.repEd)
    var repDescr: TextView = view.findViewById(R.id.repDescr)
    var eqpLbl: TextView = view.findViewById(R.id.eqpLbl)
    var eqpEd: TextView = view.findViewById(R.id.eqpEd)
    var eqpDescr: TextView = view.findViewById(R.id.eqpDescr)
    var hrsEd: TextView = view.findViewById(R.id.hrsEd)
    var eqpUnitsEd: TextView = view.findViewById(R.id.eqpUnitsEd)
    var accUnitsEd: TextView = view.findViewById(R.id.accUnitsEd)
}


class PperRecyclerViewAdapter(private var timecards: List<Timecard>) :
    RecyclerView.Adapter<PperViewHolder>() {
    private val TAG = "PperRecyclerViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PperViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emp_time, parent, false)
        return PperViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "get Item Count called")
        return if (timecards.isNotEmpty()) timecards.size else 0
    }

    fun loadNewData(newTimecards: List<Timecard>) {
        timecards = newTimecards
        notifyDataSetChanged()
    }

    fun getTimecard(position: Int): Timecard? {
        return if (timecards.isNotEmpty()) timecards[position] else null
    }


    override fun onBindViewHolder(holder: PperViewHolder, position: Int) {
        val timecard = timecards[position]

        holder.dateEd.text = timecard.date.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))

        holder.tskEd.text = timecard.tsk
        holder.tskDescr.text = timecard.tskDescr
        holder.earEd.text = timecard.ear
        holder.earDescr.text = timecard.earDescr
        if (timecard.prlSrq.trim()==""){
            holder.prlsrqLbl.visibility = View.GONE
            holder.prlsrqEd.visibility = View.GONE
            holder.prlsrqDescr.visibility = View.GONE
        }else{
            holder.prlsrqLbl.visibility = View.VISIBLE
            holder.prlsrqEd.visibility = View.VISIBLE
            holder.prlsrqDescr.visibility = View.VISIBLE
            holder.prlsrqEd.text = timecard.prlSrq
            holder.prlsrqDescr.text = timecard.prlSrqDescr
        }
        if (timecard.rdm.trim()==""){
            holder.rdmLbl.visibility = View.GONE
            holder.rdmEd.visibility = View.GONE
            holder.rdmDescr.visibility = View.GONE
        }else{
            holder.rdmLbl.visibility = View.VISIBLE
            holder.rdmEd.visibility = View.VISIBLE
            holder.rdmDescr.visibility = View.VISIBLE
            holder.rdmEd.text = timecard.rdm
            holder.rdmDescr.text = timecard.rdmDescr
        }
        if (timecard.bgm.trim()==""){
            holder.bgmLbl.visibility = View.GONE
            holder.bgmEd.visibility = View.GONE
            holder.bgmDescr.visibility = View.GONE
        }else{
            holder.bgmLbl.visibility = View.VISIBLE
            holder.bgmEd.visibility = View.VISIBLE
            holder.bgmDescr.visibility = View.VISIBLE
            holder.bgmEd.text = timecard.bgm
            holder.bgmDescr.text = timecard.bgmDescr
        }
        if (timecard.vem.trim()==""){
            holder.vemLbl.visibility = View.GONE
            holder.vemEd.visibility = View.GONE
            holder.vemDescr.visibility = View.GONE
        }else{
            holder.vemLbl.visibility = View.VISIBLE
            holder.vemEd.visibility = View.VISIBLE
            holder.vemDescr.visibility = View.VISIBLE
            holder.vemEd.text = timecard.vem
            holder.vemDescr.text = timecard.vemDescr
        }
        if (timecard.repno.trim()==""){
            holder.repLbl.visibility = View.GONE
            holder.repEd.visibility = View.GONE
            holder.repDescr.visibility = View.GONE
        }else{
            holder.repLbl.visibility = View.VISIBLE
            holder.repEd.visibility = View.VISIBLE
            holder.repDescr.visibility = View.VISIBLE
            holder.repEd.text = timecard.repno
            holder.repDescr.text = timecard.repDescr
        }
        if (timecard.eqpno.trim()==""){
            holder.eqpLbl.visibility = View.GONE
            holder.eqpEd.visibility = View.GONE
            holder.eqpDescr.visibility = View.GONE
        }else{
            holder.eqpLbl.visibility = View.VISIBLE
            holder.eqpEd.visibility = View.VISIBLE
            holder.eqpDescr.visibility = View.VISIBLE
            holder.eqpEd.text = timecard.eqpno
            holder.eqpDescr.text = timecard.eqpDescr
        }
        holder.hrsEd.text = String.format("%.2f/%.2f/%.2f",timecard.regHrs,timecard.otHrs,timecard.specHrs)
        holder.eqpUnitsEd.text = String.format("%.2f",timecard.eqpUnits)
        holder.accUnitsEd.text = String.format("%.2f",timecard.accUnits)

    }

}

