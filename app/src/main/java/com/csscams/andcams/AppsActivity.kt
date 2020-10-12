package com.csscams.andcams

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.csscams.andcams.time.TimeActivity
import kotlinx.android.synthetic.main.activity_apps.*

class AppsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apps)

        time_button.setOnClickListener{
            val intent = Intent(this, TimeActivity::class.java)
            startActivity(intent)
        }

        /*TODO: In the future will have more apps that we will store.
           I am going to forgo all DB stuff for now.  And go straight
           to the time app.
        */
        time_button.callOnClick()

    }
}
