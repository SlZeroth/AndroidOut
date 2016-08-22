package com.pjcstudio.managementpoop.Fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pjcstudio.managementpoop.Activity.SignupActivity;
import com.pjcstudio.managementpoop.DialogFrangment.SwitchingModeDialog;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.CalendarUtility;
import com.pjcstudio.managementpoop.Views.SquareImageButton;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by juchanpark on 2015. 8. 15..
 */
public class PoopRecord extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private LinearLayout timePicklayout;
    private Button setPoopbtn;
    private SquareImageButton[] imageView;
    private TextView textDate, textTime, textTimeType;
    private Bundle arg;
    private TimePickerDialog timePickerDialog;
    private String hour = "0", minute = "0";
    private String year, month, day, type, id;
    private int edittype;
    private int noworbefore = 0;

    public interface SetPoopRecordListener {
        void registerPoop(String id, String year, String month, String day, String hour, String minute, String type, int FixOrNew);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_setpooprecord, container, false);
        setInit();
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {
        timePicklayout = (LinearLayout) v.findViewById(R.id.timepicklayout);
        setPoopbtn = (Button) v.findViewById(R.id.setpoop_btn);
        timePicklayout.setOnClickListener(this);
        setPoopbtn.setOnClickListener(this);
        textDate = (TextView) v.findViewById(R.id.poop_date);
        textTime = (TextView) v.findViewById(R.id.setpoop_time);
        textTimeType = (TextView) v.findViewById(R.id.setpoop_timetype);
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.HOUR_OF_DAY) >= 0 &&  cal.get(Calendar.HOUR_OF_DAY) < 12) {
            textTimeType.setText("오전");
        } else {
            textTimeType.setText("오후");
        }

        int year = Integer.parseInt(arg.getString("year"));
        int month = Integer.parseInt(arg.getString("month"));
        int day = Integer.parseInt(arg.getString("day"));


        textTime.setText(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", cal.get(Calendar.MINUTE)));
        hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        minute = String.valueOf(cal.get(Calendar.MINUTE));
        //cal.set(Calendar.YEAR, Integer.parseInt(arg.getString("year")));
        //cal.set(Calendar.MONTH, Integer.parseInt(arg.getString("month")));
        //cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(arg.getString("day")));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date ddate = null;
        try {
            ddate = simpleDateFormat.parse(String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(ddate);
        textDate.setText(arg.getString("month") + "/" + arg.getString("day") + " " + CalendarUtility.getWeekDay(cal.get(Calendar.DAY_OF_WEEK)));
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
        imageView[0] = (SquareImageButton) v.findViewById(R.id.poop_1);
        imageView[1] = (SquareImageButton) v.findViewById(R.id.poop_2);
        imageView[2] = (SquareImageButton) v.findViewById(R.id.poop_3);
        imageView[3] = (SquareImageButton) v.findViewById(R.id.poop_4);
        imageView[4] = (SquareImageButton) v.findViewById(R.id.poop_5);
        imageView[5] = (SquareImageButton) v.findViewById(R.id.poop_6);
        imageView[0].setOnClickListener(this);
        imageView[1].setOnClickListener(this);
        imageView[2].setOnClickListener(this);
        imageView[3].setOnClickListener(this);
        imageView[4].setOnClickListener(this);
        imageView[5].setOnClickListener(this);

        Calendar sk = Calendar.getInstance();
        Log.d("daㄴㄴㄴㄴㄴㄴy", String.valueOf(day));
        if(year > sk.get(Calendar.YEAR) || (year >= sk.get(Calendar.YEAR) && month > sk.get(Calendar.MONTH)+1) || (year >= sk.get(Calendar.YEAR) && month >= sk.get(Calendar.MONTH)+1 && day > sk.get(Calendar.DAY_OF_MONTH))) {
            noworbefore = 1;
        }
    }

    private void setInit() {
        imageView = new SquareImageButton[6];
        id = null;
        arg = getArguments();
        if(arg.getString("edittype").equals("fix")) {
            id = arg.getString("id");
            edittype = 0;
        } else {
            edittype = 1;
        }
        year = arg.getString("year");
        month = arg.getString("month");
        day = arg.getString("day");
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        SquareImageButton targetImage = null;
        if(id == R.id.poop_1 || id == R.id.poop_2 || id == R.id.poop_3 || id == R.id.poop_3 || id == R.id.poop_4 || id == R.id.poop_5 ||
                id == R.id.poop_6) {
            targetImage = (SquareImageButton) view;
            imageView[0].clearColorFilter();
            imageView[1].clearColorFilter();
            imageView[2].clearColorFilter();
            imageView[3].clearColorFilter();
            imageView[4].clearColorFilter();
            imageView[5].clearColorFilter();
        }
        switch (id)
        {
            case R.id.timepicklayout:
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), "timepicker");
                break;
            case R.id.setpoop_btn:
                if(noworbefore == 0) {
                    Log.d("set", getTargetFragment().getClass().getName());
                    this.hour = String.format("%02d", Integer.parseInt(hour));
                    this.minute = String.format("%02d", Integer.parseInt(minute));
                    if (getTargetFragment().getClass().getName().equals("com.pjcstudio.managementpoop.Fragment.CalendarDayFragment")) {
                        ((CalendarDayFragment) getTargetFragment()).registerPoop(this.id, year, month, day, hour, minute, type, edittype);
                    } else {
                        ((CalendarMonthFragment) getTargetFragment()).registerPoop(this.id, year, month, day, hour, minute, type, edittype);
                    }
                    ((SwitchingModeDialog) getParentFragment()).dismiss();
                } else {
                    Toast.makeText(getActivity(), "미래시간은 입력불가능합니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.poop_1:
                type = "2";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.poop_2:
                type = "1";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.poop_3:
                type = "3";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.poop_4:
                type = "4";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.poop_5:
                type = "5";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.poop_6:
                type = "6";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int lihour, int liminute) {
        Log.d("timepick", "hour : " + hour + " minute : " + minute);
        Calendar c = Calendar.getInstance();
        int year = Integer.parseInt(arg.getString("year"));
        int month = Integer.parseInt(arg.getString("month"));
        int day = Integer.parseInt(arg.getString("day"));

        Log.d("daㄴㄴㄴㄴㄴㄴy", String.valueOf(day));
        if(year > c.get(Calendar.YEAR) || (year >= c.get(Calendar.YEAR) && month > c.get(Calendar.MONTH)+1) || (year >= c.get(Calendar.YEAR) && month >= c.get(Calendar.MONTH)+1 && day > c.get(Calendar.DAY_OF_MONTH))) {
            noworbefore = 1;
        } else if(day == c.get(Calendar.DAY_OF_MONTH)){
            Date nowTime = new Date(c.getTimeInMillis());
            c.set(Calendar.HOUR_OF_DAY, lihour);
            Date selectTime = new Date(c.getTimeInMillis());
            if (selectTime.getTime() > nowTime.getTime()) {
                noworbefore = 1;
            } else {
                noworbefore = 0;
            }
        } else {
            noworbefore = 0;
        }

        if(lihour >= 0 &&  lihour < 12) {
            textTimeType.setText("오전");
        } else {
            textTimeType.setText("오후");
        }
        String fohour = String.format("%02d", lihour);
        String fominute = String.format("%02d", liminute);
        textTime.setText(fohour + ":" + fominute);
        this.hour = fohour;
        this.minute = fominute;
    }
}
