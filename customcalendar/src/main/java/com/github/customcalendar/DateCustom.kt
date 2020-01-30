package com.github.customcalendar

import java.util.*

data class DateCustom(
        private var year: Int,
        private var month: Int,
        private var date: Int) : Date(year,month,date){

    override fun getYear() = year
    override fun getDate() = date
    override fun getMonth() = month
    override fun setYear(year: Int) { super.setYear(year)}
    override fun setMonth(month: Int) { super.setMonth(month) }
    override fun setDate(date: Int) { super.setDate(date) }

    var enableDay:Boolean = java.lang.Boolean.FALSE
    var dummyDay:Boolean = java.lang.Boolean.FALSE
    var firstBarShow:BarStatus = BarStatus.HIDE
    var secondBarShow:BarStatus = BarStatus.HIDE
}