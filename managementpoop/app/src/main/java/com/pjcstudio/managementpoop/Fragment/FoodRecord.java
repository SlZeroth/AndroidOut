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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class FoodRecord extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{

    private LinearLayout timePicklayout;
    private SquareImageButton[] imageView;
    private Button setFoodbtn;
    private TextView textDate, textTime, textTimeType;
    private EditText editMemo;
    private TimePickerDialog timePickerDialog;
    private String memo, type, id;
    private String mHour, mMinute;
    private Bundle arg;
    private int edittype;
    private int noworbefore = 0; // 0 정상 1 미래

    public interface SetFoodRecordListener {
        void registerFood(String id, String year, String month, String day, String hour, String minute, String memo, String type, int FixOrNew);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_setfoodrecord, container, false);
        setInit();
        setLayout(rootView);
        return rootView;
    }

    private void setInit() {
        id = null;
        arg = getArguments();
        if(arg.getString("edittype").equals("fix")) {
            edittype = 0;
            id = arg.getString("id");
        } else {
            edittype = 1;
        }
        imageView = new SquareImageButton[6];
        Log.d("year", arg.getString("year"));
    }

    private void setLayout(View v) {
        Log.d("SetFood", "setLayout");
        timePicklayout = (LinearLayout) v.findViewById(R.id.timepicklayouts);
        timePicklayout.setOnClickListener(this);
        setFoodbtn = (Button) v.findViewById(R.id.setfood_btn);
        editMemo = (EditText) v.findViewById(R.id.food_memo);
        textDate = (TextView) v.findViewById(R.id.fooddate);
        textTime = (TextView) v.findViewById(R.id.food_time);
        textTimeType = (TextView) v.findViewById(R.id.food_timetype);

        int year = Integer.parseInt(arg.getString("year"));
        int month = Integer.parseInt(arg.getString("month"));
        int day = Integer.parseInt(arg.getString("day"));


        Calendar cal = Calendar.getInstance();
        textTime.setText(String.format("%02d", cal.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", cal.get(Calendar.MINUTE)));
        if(cal.get(Calendar.HOUR_OF_DAY) >= 0 &&  cal.get(Calendar.HOUR_OF_DAY) < 12) {
            textTimeType.setText("오전");
        } else {
            textTimeType.setText("오후");
        }
        mHour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        mMinute = String.valueOf(cal.get(Calendar.MINUTE));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date ddate = null;
        try {
            ddate = simpleDateFormat.parse(String.format("%04d", year) + String.format("%02d", month) + String.format("%02d", day));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(ddate);
        textDate.setText(arg.getString("month") + "/" + arg.getString("day") + " " + CalendarUtility.getWeekDay(cal.get(Calendar.DAY_OF_WEEK)));
        setFoodbtn.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        imageView[0] = (SquareImageButton) v.findViewById(R.id.food_1);
        imageView[1] = (SquareImageButton) v.findViewById(R.id.food_2);
        imageView[2] = (SquareImageButton) v.findViewById(R.id.food_3);
        imageView[3] = (SquareImageButton) v.findViewById(R.id.food_4);
        imageView[4] = (SquareImageButton) v.findViewById(R.id.food_5);
        imageView[5] = (SquareImageButton) v.findViewById(R.id.food_6);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        SquareImageButton targetImage = null;
        if(id == R.id.food_1 || id == R.id.food_2 || id == R.id.food_3 || id == R.id.food_3 || id == R.id.food_4 || id == R.id.food_5 ||
                id == R.id.food_6) {
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
            case R.id.timepicklayouts:
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), "timepicker");
                break;
            case R.id.setfood_btn:
                if(noworbefore == 0) {
                    mHour = String.format("%02d", Integer.parseInt(mHour));
                    mMinute = String.format("%02d", Integer.parseInt(mMinute));
                    memo = editMemo.getText().toString();
                    if (memo.equals("")) {
                        memo = " ";
                    }
                    if (getTargetFragment().getClass().getName().equals("com.pjcstudio.managementpoop.Fragment.CalendarDayFragment")) {
                        ((CalendarDayFragment) getTargetFragment()).registerFood(
                                this.id,
                                arg.getString("year"),
                                arg.getString("month"),
                                arg.getString("day"),
                                mHour,
                                mMinute,
                                memo,
                                type,
                                edittype
                        );
                    } else {
                        ((CalendarMonthFragment) getTargetFragment()).registerFood(
                                this.id,
                                arg.getString("year"),
                                arg.getString("month"),
                                arg.getString("day"),
                                mHour,
                                mMinute,
                                memo,
                                type,
                                edittype
                        );
                    }
                    ((SwitchingModeDialog) getParentFragment()).dismiss();
                } else {
                    Toast.makeText(getActivity(), "미래시간은 입력불가능합니다.", Toast.LENGTH_SHORT).show();
                }
                //dismiss();
                break;
            case R.id.food_1:
                type = "1";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.food_2:
                type = "2";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.food_3:
                type = "3";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.food_4:
                type = "4";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.food_5:
                type = "5";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
            case R.id.food_6:
                type = "6";
                targetImage.setColorFilter(0xaa111111, PorterDuff.Mode.SRC_ATOP);
                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int onHour, int onMinute) {
        textTime.setText(onHour + ":" + onMinute);
        Calendar c = Calendar.getInstance();
        int year = Integer.parseInt(arg.getString("year"));
        int month = Integer.parseInt(arg.getString("month"));
        int day = Integer.parseInt(arg.getString("day"));

        Log.d("daㄴㄴㄴㄴㄴㄴy", String.valueOf(day));
        if(year > c.get(Calendar.YEAR) || (year >= c.get(Calendar.YEAR) && month > c.get(Calendar.MONTH)+1) || (year >= c.get(Calendar.YEAR) && month >= c.get(Calendar.MONTH)+1 && day > c.get(Calendar.DAY_OF_MONTH))) {
            noworbefore = 1;
        } else if(day == c.get(Calendar.DAY_OF_MONTH)){
            Date nowTime = new Date(c.getTimeInMillis());
            c.set(Calendar.HOUR_OF_DAY, onHour);
            Date selectTime = new Date(c.getTimeInMillis());
            if (selectTime.getTime() > nowTime.getTime()) {
                noworbefore = 1;
            } else {
                noworbefore = 0;
            }
        } else {
            noworbefore = 0;
        }
        if(onHour >= 0 &&  onHour < 12) {
            textTimeType.setText("오전");
        } else {
            textTimeType.setText("오후");
        }
        String fohour = String.format("%02d", onHour);
        String fominute = String.format("%02d", onMinute);
        mHour = fohour;
        mMinute = fominute;
        textTime.setText(fohour + ":" + fominute);
    }
}
