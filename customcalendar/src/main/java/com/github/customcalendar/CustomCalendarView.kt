package com.github.customcalendar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import com.github.customcalendar.AttrsSetColors.firstColorBar
import com.github.customcalendar.AttrsSetColors.secondColorBar
import java.text.SimpleDateFormat
import java.util.*

/**
 * Scope: Calendar with week start MON to SUN
 * default show 6 week
 */
class CustomCalendarView : LinearLayout {
    private var currentDate: TextView? = null
    private var calendarGridView: GridView? = null
    private var nextButton: ImageView? = null
    private var prevButton: ImageView? = null
    private var Column: Int? = 42 //setupCalendar 6 week
    private var dayNumber=0
    private val formatter = SimpleDateFormat("MMMM", Locale.getDefault())
    private var context1: Context? = null
    private val dayValueInCells: MutableList<DateCustom> = ArrayList()
    private var onClickListener: OnClickListenerCalendar? = null
    private var adapter: GridAdapter? = null
    private var paramsLayout: TypedArray? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        this.context1 = context
        setupCalendar()
        paramsLayout = context.theme.obtainStyledAttributes(attrs, R.styleable.custom_calendar_attrs, 0, 0)
        Column = paramsLayout?.getInteger(R.styleable.custom_calendar_attrs_weeks, 6)
        Column=Column!!*7
        firstColorBar = paramsLayout?.getColor(R.styleable.custom_calendar_attrs_firstBarColor, Color.RED)
        secondColorBar = paramsLayout?.getColor(R.styleable.custom_calendar_attrs_secondBarColor, Color.BLUE)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    /**
     * set number of week you want show
     */
    private fun setWeeks(weeks: Int) {
        Column = weeks * 7
    }

    /**
     * Set color of first bar
     */
    private fun setFirstBarColor(color: Int) {
        firstColorBar = color
    }

    /**
     * Set color of second bar
     */
    private fun setSecondBarColor(color: Int) {
        secondColorBar = color
    }

    /**
     * Create calendar with custom list, you can set all attribute in day, like bar visibility and color
     */
    fun setCalendar(customDate: List<DateCustom>) {
        currentDate!!.text = formatter.format(cal.time)
        dayValueInCells.clear()
        dayNumber = Column!!
        val mCal = cal.clone() as Calendar
        mCal[Calendar.DAY_OF_MONTH] = 1 //first day of month
        val firstDayOfWeek = mCal[Calendar.DAY_OF_WEEK] //find first day of week
        mCal.add(Calendar.DAY_OF_MONTH, -beforeDay(firstDayOfWeek))
        val beforeDayList = addDummyStartDay(mCal, firstDayOfWeek)
        dayNumber = Column!! - beforeDayList.size - customDate.size //days left to add
        val afterDayList = addDummyEndDay(mCal, customDate[customDate.size - 1])
        dayValueInCells.clear()
        dayValueInCells.addAll(beforeDayList) //add dummy day before current month
        dayValueInCells.addAll(customDate) //add current month
        dayValueInCells.addAll(afterDayList) //add dummy day after current month
        adapter = GridAdapter(context1, dayValueInCells, cal, onClickListener) //create adapter
        calendarGridView!!.adapter = adapter //set adapter
    }

    /**
     * Set custom listener
     * @param onClickListener my listener
     */
    fun setOnClickListener(onClickListener: OnClickListenerCalendar) {
        this.onClickListener = onClickListener
    }

    /**
     * Add dummy day at the end of month
     * @param mCal today calendar
     * @param lastDayOfMonth last day of month
     * @return list of day with dummy day at the end
     */
    private fun addDummyEndDay(mCal: Calendar, lastDayOfMonth: DateCustom): List<DateCustom> {
        val afterDayList: MutableList<DateCustom> = ArrayList()
        mCal[Calendar.DAY_OF_MONTH] = lastDayOfMonth.date
        for (i in 0 until dayNumber) {
            mCal.add(Calendar.DAY_OF_MONTH, 1)
            val d = DateCustom(mCal[Calendar.YEAR], mCal[Calendar.MONTH], mCal[Calendar.DAY_OF_MONTH])
            d.dummyDay = true //set dummy day
            afterDayList.add(d)
        }
        return afterDayList
    }

    /**
     * Add dummy day at the start of month
     * @param mCal today calendar
     * @param firstDayOfWeek first day of month
     * @return list of day with dummy day at the start
     */
    private fun addDummyStartDay(mCal: Calendar, firstDayOfWeek: Int): List<DateCustom> {
        val beforeDayList: MutableList<DateCustom> = ArrayList()
        while (beforeDayList.size < beforeDay(firstDayOfWeek)) {
            val d = DateCustom(mCal[Calendar.YEAR], mCal[Calendar.MONTH], mCal[Calendar.DAY_OF_MONTH])
            d.dummyDay = true
            beforeDayList.add(d)
            mCal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return beforeDayList
    }

    private fun setPreviousButtonClickEvent() {
        prevButton!!.setOnClickListener {
            cal.add(Calendar.MONTH, -1)
            setCalendar(getDayList(cal[Calendar.MONTH], cal[Calendar.YEAR]))
        }
    }

    private fun setNextButtonClickEvent() {
        nextButton!!.setOnClickListener {
            cal.add(Calendar.MONTH, 1)
            setCalendar(getDayList(cal[Calendar.MONTH], cal[Calendar.YEAR]))
        }
    }

    /**
     * get select day
     * @return select day in calendar
     */
    @get:Throws(NullPointerException::class)
    val selectDate: DateCustom?
        get() = if (calendarGridView!!.adapter != null) {
            (calendarGridView!!.adapter as GridAdapter).getSelectDate()
        } else {
            Log.e(TAG, "adapter doesn't sent")
            throw EmptyStackException()
        }

    private fun beforeDay(firstDay: Int): Int {
        return if (firstDay - 2 < 0) 6 else firstDay - 2
    }

    /**
     * setup calendar
     */
    private fun setupCalendar() {
        initializeUILayout()
        setCalendar(getDayList(cal[Calendar.MONTH], cal[Calendar.YEAR]))
        setPreviousButtonClickEvent()
        setNextButtonClickEvent()
    }


    private fun initializeUILayout() {
        val inflater = context1!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.calendar_layout, this)
        currentDate = view.findViewById(R.id.display_current_date)
        calendarGridView = view.findViewById(R.id.calendar_grid)
        prevButton = view.findViewById(R.id.img_back)
        nextButton = view.findViewById(R.id.img_next)
    }

    companion object {
        private val TAG = CustomCalendarView::class.java.name
        private val cal = Calendar.getInstance(Locale.ITALY)

        /**
         * Get list of days in month
         * @param month da 0 a 11
         * @param year year
         * @return list of days in month
         */
        fun getDayList(month: Int, year: Int): List<DateCustom> {
            cal[Calendar.DAY_OF_MONTH] = 1
            cal[Calendar.MONTH] = month
            cal[Calendar.YEAR] = year
            val dayList: MutableList<DateCustom> = ArrayList()
            for (i in 1 until cal.getActualMaximum(Calendar.DAY_OF_MONTH) + 1) {
                val d = DateCustom(year, month, i)
                d.enableDay = true
                dayList.add(d)
            }
            return dayList
        }
    }
}

