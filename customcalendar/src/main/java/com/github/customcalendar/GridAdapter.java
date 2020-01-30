package com.github.customcalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GridAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private LinearLayout cellLayout;
    private View prevView;
    private TextView lblDateId, eventIndicatorFirst, eventIndicatorSecond;;
    private List<DateCustom> monthlyDates;
    private DateCustom selectDate;
    private Calendar calFromWeb;
    private Calendar today = Calendar.getInstance(Locale.ITALY);
    private DateCustom mDate;
    private OnClickListenerCalendar onClickListener;

    public GridAdapter(Context context, List<DateCustom> monthlyDates, Calendar currentDate, OnClickListenerCalendar onClickListener) {
        super();
        this.monthlyDates = monthlyDates;
        this.calFromWeb = currentDate;
        this.mInflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mDate = monthlyDates.get(position);
        int dayValue = calFromWeb.get(Calendar.DAY_OF_MONTH);
        int displayMonth = calFromWeb.get(Calendar.MONTH);
        int displayYear = calFromWeb.get(Calendar.YEAR);


        if(convertView == null){
            convertView =  mInflater.inflate(R.layout.single_cell_enable_day, parent, false);
        }

        setupUI(convertView);
        lblDateId.setText(dayValue < 10 ? mDate.getDate() + "  " : String.valueOf(mDate.getDate()));

        if (displayMonth == mDate.getMonth() && displayYear == mDate.getYear()) {
            lblDateId.setTextColor(ContextCompat.getColor(convertView.getContext(),R.color.txt_color_day_off));

            convertView.setOnClickListener(new onClickListenerCustom(){
                @Override
                public void onClick(View v) {
                    super.onClick(v);
                    selectDate = monthlyDates.get(position);
                    if(onClickListener != null){
                       onClickListener.onClickListenerCalendar(v,selectDate);
                    }
                }
            });
            displayDayOff(convertView);
            drawCurrentDay(convertView);
        }else{
            convertView.setEnabled(false);
        }
        displayFirstBar();
        displaySecondBar();



        return convertView;
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }

    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }




    public DateCustom getSelectDate(){
        if(selectDate==null){
            int day = today.getTime().getDate();
            int month = today.getTime().getMonth();

            for (DateCustom d:monthlyDates) {
                if (d.getDate() == day && d.getMonth() == month) {
                    selectDate = d;
                    break;
                }
            }
        }
        return selectDate;
    }




    private boolean dayStatus(DateCustom day){
        return day!=null&&day.getEnableDay();
    }

    private void drawSelectCell(View v) {
        prevView = v;
        LinearLayout cellLayout = v.findViewById(R.id.cell_layout);
        cellLayout.setBackground(v.getResources().getDrawable(R.drawable.stroke_single_cell_select));
    }


    private void drawUnselectCell(View v) {
        LinearLayout cellLayout = v.findViewById(R.id.cell_layout);
        if(dayStatus(selectDate)) {
            cellLayout.setBackground(v.getResources().getDrawable(R.drawable.stroke_single_cell_day_off));
        }else{
           cellLayout.setBackground(v.getResources().getDrawable(R.drawable.stroke_single_cell));
        }

    }

    /**
     * Draw day off with different background
     * @param v view
     */
    private void displayDayOff(View v){
        if(dayStatus(mDate)){
            lblDateId.setTextColor(ContextCompat.getColor(v.getContext(),R.color.txt_color));
            cellLayout.setBackground(v.getResources().getDrawable(R.drawable.stroke_single_cell_day_off));
        }
    }


    /**
     * draw first bar of day
     */
    private void displayFirstBar(){
        switch (mDate.getFirstBarShow()){
            case FIRSTCOLOR:
                eventIndicatorFirst.setBackgroundColor(AttrsSetColors.INSTANCE.getFirstColorBar());
                break;

            case SECONDCOLOR:
                eventIndicatorFirst.setBackgroundColor(AttrsSetColors.INSTANCE.getSecondColorBar());
                break;


                default:
                    eventIndicatorFirst.setBackgroundColor(0);
                    break;
        }

    }

    /**
     * draw second bar of day
     */
    private void displaySecondBar(){
        switch (mDate.getSecondBarShow()){
            case FIRSTCOLOR:
                eventIndicatorSecond.setBackgroundColor(AttrsSetColors.INSTANCE.getFirstColorBar());
                break;

            case SECONDCOLOR:
                eventIndicatorSecond.setBackgroundColor(AttrsSetColors.INSTANCE.getSecondColorBar());
                break;

                default:
                    eventIndicatorSecond.setBackgroundColor(0);
                    break;

        }
    }


    /**
     * select current day
     * @param view view
     */
    private void drawCurrentDay(View view){
        int day = today.getTime().getDate();
        int month = today.getTime().getMonth();
        if(mDate.getDate() == day && mDate.getMonth() ==month) {
            drawSelectCell(view);
            selectDate = mDate;
        }

    }

    private void setupUI(View view){
        cellLayout=view.findViewById(R.id.cell_layout);
        lblDateId = view.findViewById(R.id.calendar_date_id);
        eventIndicatorFirst = view.findViewById(R.id.first_event_bar);
        eventIndicatorSecond = view.findViewById(R.id.second_event_bar);
    }




    public class onClickListenerCustom implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (prevView != null){
               drawUnselectCell(prevView);
            }
            drawSelectCell(v);
        }
    }

}
