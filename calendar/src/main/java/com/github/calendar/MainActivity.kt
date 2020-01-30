package com.alberto.calendar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.customcalendar.BarStatus
import com.github.customcalendar.CustomCalendarView
import com.github.customcalendar.DateCustom
import com.github.customcalendar.OnClickListenerCalendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val customCalendar = findViewById<CustomCalendarView>(R.id.custom_calendar)
        val listDay = CustomCalendarView.getDayList(0,2020) //month 0 - 12
        listDay[0].firstBarShow = BarStatus.SECONDCOLOR
        listDay[listDay.size-1].secondBarShow = BarStatus.FIRSTCOLOR


        customCalendar.setOnClickListener(object : OnClickListenerCalendar {
            override fun onClickListenerCalendar(v: View, d: DateCustom) {
                Toast.makeText(applicationContext,d.toString(),Toast.LENGTH_SHORT).show()
            }
        })

        customCalendar.setCalendar(listDay)
    }
}