package com.csscams.andcams.ui.daily

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.csscams.andcams.R

class DailyFragment : Fragment() {

    private lateinit var dailyViewModel: DailyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dailyViewModel =
            ViewModelProviders.of(this).get(DailyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_daily, container, false)
        val textView: TextView = root.findViewById(R.id.text_daily)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Daily Timecard"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "01/01/2020"
        dailyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it

        })
        return root
    }

}
