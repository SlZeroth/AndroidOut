package com.pjcstudio.managementpoop.Views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pjcstudio.managementpoop.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by juchanpark on 2015. 7. 28..
 */
public class CalendarView extends LinearLayout implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private Calendar mCalendar;
    private CalendarViewListener calendarViewListener;
    private ArrayList<LinearLayout> linearrealday = new ArrayList<LinearLayout>();
    private ArrayList<ImageView> Imagerealday = new ArrayList<ImageView>();
    private int nowyear, nowmonth;
    static final int ITEMTYPE_CLICK = 0;
    static final int ITEMTYPE_NOCLICK = 1;
    private int tyear, tmonth, tday;

    public interface CalendarViewListener {
        int DayClickListener(int year, int month, int day);
        int DayLongClickListener(int year, int month, int day);
    }

    public CalendarView(Context context) {
        super(context);

        mContext = context;
        //CalendarInit(2015, 7);
        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public CalendarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        //CalendarInit(2015, 7);
        setBackgroundColor(Color.WHITE);
        setOrientation(LinearLayout.VERTICAL);
        setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void setClickListener(CalendarViewListener listener) {
        calendarViewListener = listener;
    }

    public void InitCalendar(int year, int month) {

        Calendar cal = Calendar.getInstance();
        Log.i("YEAR", String.valueOf(year));
        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 1);

        Calendar calToday = Calendar.getInstance();
        tyear = calToday.get(Calendar.YEAR);
        tmonth = calToday.get(Calendar.MONTH)+1;
        tday = calToday.get(Calendar.DATE);
        Log.d("DAYDAYDAYDAY", String.valueOf(tyear) + " : " + String.valueOf(tmonth) + " : " + String.valueOf(tday));

        int startDay = cal.get(Calendar.DAY_OF_WEEK); // 월 시작 요일
        nowyear = cal.get(Calendar.YEAR);
        nowmonth = cal.get(Calendar.MONTH)+1;
        Log.d("nownownownow", nowyear + " : " + nowmonth);
        int lastDay = cal.getActualMaximum(Calendar.DATE); // 월 마지막 날짜

        int inputDate=1;

        Calendar lastCal = Calendar.getInstance();
        lastCal.add(Calendar.YEAR, year);
        lastCal.add(Calendar.MONTH, month-1);
        lastCal.set(Calendar.DATE, 1);

        int lastMonthDay = lastCal.getActualMaximum(Calendar.DATE);

        Log.d("CalendarView", "date : "+year+"/"+month);
        Log.d("CalendarView","startDay : "+startDay);
        Log.d("CalendarView","lastDay : "+lastDay);
        Log.d("CalendarView","lastMonthDay : "+lastMonthDay);


        //첫번재줄(이전달+요번달 시작1주)
        LinearLayout layoutWeek = new LinearLayout(mContext);
        LinearLayout.LayoutParams paramsSubroot = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        paramsSubroot.weight = 1.0f;
        layoutWeek.setLayoutParams(paramsSubroot);
        layoutWeek.setOrientation(LinearLayout.HORIZONTAL);

        for(int i=0;i<startDay-1;i++){ //전달 ~전달마지막날
            View dayView = MakeItemday(i+lastMonthDay-startDay+2, ITEMTYPE_NOCLICK);
            layoutWeek.addView(dayView);
        }
        for(int i=0;i<7-startDay+1;i++){ //요번달 1일~
            View dayView = MakeItemday(inputDate, ITEMTYPE_CLICK);
            linearrealday.add((LinearLayout) dayView);
            layoutWeek.addView(dayView);
            inputDate++;

        }
        addView(layoutWeek);

        //두번째줄~마지막줄전줄
        for(;;){
            if(lastDay-inputDate<7) break;
            layoutWeek = new LinearLayout(mContext);
            layoutWeek.setLayoutParams(paramsSubroot);
            layoutWeek.setOrientation(LinearLayout.HORIZONTAL);
            for(int i=0;i<7;i++){
                View dayView = MakeItemday(inputDate, ITEMTYPE_CLICK);
                linearrealday.add((LinearLayout) dayView);
                layoutWeek.addView(dayView);
                inputDate++;
            }
            addView(layoutWeek);
        }

        //마지막줄
        layoutWeek = new LinearLayout(mContext);
        layoutWeek.setLayoutParams(paramsSubroot);
        layoutWeek.setOrientation(LinearLayout.HORIZONTAL);
        int otherweekflag = 0;
        for(int i=0;i<7;i++){
            View dayView = null;
            if(otherweekflag == 0) {
                dayView = MakeItemday(inputDate, ITEMTYPE_CLICK); //요번달 마지막날까지
                linearrealday.add((LinearLayout) dayView);
            } else {
                dayView = MakeItemday(inputDate, ITEMTYPE_NOCLICK); //요번달 마지막날까지
                linearrealday.add((LinearLayout) dayView);
            }
            if(inputDate>lastDay){ //다음달 1~
                otherweekflag = 1;
                dayView = MakeItemday(1, ITEMTYPE_NOCLICK);
                inputDate=1;
            }
            inputDate++;
            layoutWeek.addView(dayView);
        }
        addView(layoutWeek);

        /*
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        //calendar.set(Calendar.YEAR, year);
        //calendar.set(Calendar.MONTH, month);
        int endday = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        int dayofweek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.i("endofday", "day : " + endday);

        int daystart = 1;
        //Toast.makeText(mContext, String.valueOf(calendar.getMaximum(Calendar.DAY_OF_MONTH)), Toast.LENGTH_LONG).show();
        for(int height=0;height<5;height++) { // 리니어레이아웃 5 * 7 배열
            LinearLayout linearLayoutSubroot = new LinearLayout(mContext);
            LinearLayout.LayoutParams paramsSubroot = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
            paramsSubroot.weight = 1.0f;
            linearLayoutSubroot.setLayoutParams(paramsSubroot);
            linearLayoutSubroot.setOrientation(LinearLayout.HORIZONTAL);
            for(int width=0;width<7;width++) {
                if(daystart > endday) {
                    daystart = 1;
                }
                View dayView = MakeItemday(daystart);
                linearLayoutSubroot.addView(dayView);
                daystart++;
            }
            linearLayoutWeek.add(linearLayoutSubroot);
        }
        for(int i=0;i<linearLayoutWeek.size();i++) {
            addView(linearLayoutWeek.get(i));
        }*/
    }

    private View MakeItemday(int day, int type) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layoutParamtext = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParamtext.setMargins(0, 10, 0, 0);
        layoutParams.setMargins(1,1,1,1);
        layoutParams.weight = 1.0f;
        linearLayout.setBackgroundResource(R.drawable.calendaritem);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(mContext);
        if(type == ITEMTYPE_CLICK) {
            linearLayout.setOnClickListener(this);
            linearLayout.setOnLongClickListener(this);
            linearLayout.setTag(day);
            if(nowyear == tyear && nowmonth == tmonth && day == tday) {
                textView.setTextColor(Color.parseColor("#4CBACE"));
            } else {
                textView.setTextColor(Color.parseColor("#696969"));
            }
        } else if(type == ITEMTYPE_NOCLICK) {
            textView.setTextColor(Color.parseColor("#A9A9A9"));
        }
        textView.setGravity(Gravity.TOP|Gravity.CENTER);
        textView.setLayoutParams(layoutParamtext);
        textView.setText(String.valueOf(day));
        linearLayout.addView(textView);

        ImageView imageView = new ImageView(mContext);
        imageView.setTag("image");
        final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams(width, height);
        imageView.setLayoutParams(paramsImage);
        //imageView.setImageResource(R.drawable.ico_dung2);

        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams paramsrel = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(paramsrel);
        RelativeLayout relLayoutend = new RelativeLayout(mContext);
        relLayoutend.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        relLayoutend.setVerticalGravity(Gravity.CENTER_VERTICAL);
        relLayoutend.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        relLayoutend.addView(imageView);
        relativeLayout.addView(relLayoutend);
        linearLayout.addView(relativeLayout);



        return linearLayout;
    }

    public int CalendarInit(int Year, int Month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Year);
        calendar.set(Calendar.MONTH, Month);
        Log.i("DAY", "NUM" + calendar.getMaximum(Calendar.DAY_OF_MONTH));
        return 1;
    }

    public int CalendarFixday(int days, int fixtype, String itemtype, int itemnum) {
        if(fixtype == 0) {
            LinearLayout lin = linearrealday.get(days-1);
            lin.setBackgroundColor(Color.parseColor("#e2e3e7"));
            ImageView image = (ImageView) lin.findViewWithTag("image");
            image.setVisibility(INVISIBLE);
        }
        if(fixtype == 1) {
            LinearLayout lin = linearrealday.get(days-1);
            //lin.setBackgroundColor(Color.parseColor("#DAA520"));
            ImageView image = (ImageView) lin.findViewWithTag("image");
            if(itemtype.equals("P")) {
                switch (itemnum)
                {
                    case 1:
                        image.setImageResource(R.drawable.poop_1);
                        break;
                    case 2:
                        image.setImageResource(R.drawable.poop_2);
                        break;
                    case 3:
                        image.setImageResource(R.drawable.poop_3);
                        break;
                    case 4:
                        image.setImageResource(R.drawable.poop_4);
                        break;
                    case 5:
                        image.setImageResource(R.drawable.poop_5);
                        break;
                    case 6:
                        image.setImageResource(R.drawable.poop_6);
                        break;
                }
            } else if(itemtype.equals("F")) {
                switch (itemnum)
                {
                    case 1:
                        image.setImageResource(R.drawable.food_1);
                        break;
                    case 2:
                        image.setImageResource(R.drawable.food_2);
                        break;
                    case 3:
                        image.setImageResource(R.drawable.food_3);
                        break;
                    case 4:
                        image.setImageResource(R.drawable.food_4);
                        break;
                    case 5:
                        image.setImageResource(R.drawable.food_5);
                        break;
                    case 6:
                        image.setImageResource(R.drawable.food_6);
                        break;
                }
            }
        }
        return 1;
    }

    @Override
    public void onClick(View view) {
        LinearLayout v = (LinearLayout) view;
        calendarViewListener.DayClickListener(nowyear, nowmonth, (int)v.getTag());
    }

    @Override
    public boolean onLongClick(View view) {
        calendarViewListener.DayLongClickListener(nowyear, nowmonth, (int) view.getTag());
        return true;
    }
}
