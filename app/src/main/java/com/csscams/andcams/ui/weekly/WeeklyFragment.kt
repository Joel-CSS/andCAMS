package com.csscams.andcams.ui.weekly

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

class WeeklyFragment : Fragment() {

    private lateinit var weeklyViewModel: WeeklyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weeklyViewModel =
            ViewModelProviders.of(this).get(WeeklyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_weekly, container, false)
        val textView: TextView = root.findViewById(R.id.text_weekly)
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Weekly Timecard"
        (activity as? AppCompatActivity)?.supportActionBar?.subtitle = "01/01/2020~01/01/2020"
        weeklyViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}

