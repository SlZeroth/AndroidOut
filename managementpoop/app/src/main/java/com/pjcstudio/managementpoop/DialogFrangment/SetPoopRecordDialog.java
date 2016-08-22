package com.pjcstudio.managementpoop.DialogFrangment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pjcstudio.managementpoop.Activity.MainActivity;
import com.pjcstudio.managementpoop.Fragment.CalendarMonthFragment;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.CalendarUtility;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by juchanpark on 2015. 8. 6..
 */
public class SetPoopRecordDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {

    private LinearLayout timePicklayout;
    private Button setPoopbtn;
    private TextView textDate, textTime;
    private Bundle arg;
    private TimePickerDialog timePickerDialog;
    private String hour = "0", minute = "0";
    private String year, month, day, type;

    public interface SetPoopRecordListener {
        void registerPoop(String year, String month, String day, String hour, String minute, String type);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(arg.getString("year")));
        cal.set(Calendar.MONTH, Integer.parseInt(arg.getString("month")));
        cal.set(Calendar.DATE, Integer.parseInt(arg.getString("day")));
        textDate.setText(arg.getString("month") + "/" + arg.getString("day") + " " + CalendarUtility.getWeekDay(cal.get(Calendar.DAY_OF_WEEK)));
        Calendar calendar = Calendar.getInstance();
        timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);
        ((Button)v.findViewById(R.id.poop_1)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.poop_2)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.poop_3)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.poop_4)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.poop_5)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.poop_6)).setOnClickListener(this);
    }

    private void setInit() {
        arg = getArguments();
        year = arg.getString("year");
        month = arg.getString("month");
        day = arg.getString("day");
    }

    @Override
    public void onClick(View view) {
        int Vid = view.getId();
        Button btns = (Button) view;
        switch (Vid)
        {
            case R.id.timepicklayout:
                timePickerDialog.setVibrate(false);
                timePickerDialog.setCloseOnSingleTapMinute(false);
                timePickerDialog.show(getActivity().getSupportFragmentManager(), "timepicker");
                break;
            case R.id.setpoop_btn:
                SetPoopRecordListener setPoopRecordListener = (SetPoopRecordListener) getTargetFragment();
                setPoopRecordListener.registerPoop(year, month, day, hour, minute, type);
                dismiss();
                break;
            case R.id.poop_1:
                type = "1";
                break;
            case R.id.poop_2:
                type = "2";
                break;
            case R.id.poop_3:
                type = "3";
                break;
            case R.id.poop_4:
                type = "4";
                break;
            case R.id.poop_5:
                type = "5";
                break;
            case R.id.poop_6:
                type = "6";
                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int lihour, int liminute) {
        Log.d("timepick", "hour : " + hour + " minute : " + minute);
        textTime.setText(lihour + ":" + liminute);
        this.hour = String.valueOf(lihour);
        this.minute = String.valueOf(liminute);
    }
}
