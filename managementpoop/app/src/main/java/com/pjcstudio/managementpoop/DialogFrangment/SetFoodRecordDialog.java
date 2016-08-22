package com.pjcstudio.managementpoop.DialogFrangment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.CalendarUtility;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by juchanpark on 2015. 8. 8..
 */
public class SetFoodRecordDialog extends DialogFragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private LinearLayout timePicklayout;
    private Button setFoodbtn;
    private TextView textDate, textTime;
    private EditText editMemo;
    private TimePickerDialog timePickerDialog;
    private String memo, type;
    private int mHour, mMinute;
    private Bundle arg;

    public interface SetFoodRecordListener {
        void registerFood(String year, String month, String day, String hour, String minute, String memo, String type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialog_setfoodrecord, container, false);
        setInit();
        setLayout(rootView);
        return rootView;
    }

    private void setInit() {
        arg = getArguments();
        if(arg.getString("year") == null) {
            Log.d("year", "nohas");
        }
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
        Calendar cal = Calendar.getInstance();
        textTime.setText(cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE));
        cal.set(Calendar.YEAR, Integer.parseInt(arg.getString("year")));
        cal.set(Calendar.MONTH, Integer.parseInt(arg.getString("month")));
        cal.set(Calendar.DATE, Integer.parseInt(arg.getString("day")));
        textDate.setText(arg.getString("month") + "/" + arg.getString("day") + " " + CalendarUtility.getWeekDay(cal.get(Calendar.DAY_OF_WEEK)));
        setFoodbtn.setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        ((Button)v.findViewById(R.id.food_1)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.food_2)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.food_3)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.food_4)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.food_5)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.food_6)).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.timepicklayouts:
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), "timepicker");
                break;
            case R.id.setfood_btn:
                memo = editMemo.getText().toString();
                if(!memo.equals("")) {
                    SetFoodRecordListener setFoodRecordListener = (SetFoodRecordListener) getTargetFragment();
                    setFoodRecordListener.registerFood(
                            arg.getString("year"),
                            arg.getString("month"),
                            arg.getString("day"),
                            String.valueOf(mHour),
                            String.valueOf(mMinute),
                            memo,
                            type
                    );
                } else {
                    Toast.makeText(getActivity(), "텍스트를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                dismiss();
                break;
            case R.id.food_1:
                type = "1";
                break;
            case R.id.food_2:
                type = "2";
                break;
            case R.id.food_3:
                type = "3";
                break;
            case R.id.food_4:
                type = "4";
                break;
            case R.id.food_5:
                type = "5";
                break;
            case R.id.food_6:
                type = "6";
                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int onHour, int onMinute) {
        textTime.setText(onHour + ":" + onMinute);
        mHour = onHour;
        mMinute = onMinute;
    }
}
