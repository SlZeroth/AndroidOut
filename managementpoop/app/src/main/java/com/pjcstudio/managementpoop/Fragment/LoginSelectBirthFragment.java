package com.pjcstudio.managementpoop.Fragment;

import android.app.DatePickerDialog;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;

import com.pjcstudio.managementpoop.Activity.LoginActivity;
import com.pjcstudio.managementpoop.Activity.SignupActivity;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.BitmapTool;

import java.util.Calendar;

/**
 * Created by juchanpark on 2015. 7. 25..
 */
public class LoginSelectBirthFragment extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private RelativeLayout relativeLayout;
    private int year, month , day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_select_birth, container, false);
        setLayout(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.d("onDestory", "LoginSelectBirthFragment");
        relativeLayout.setBackground(null);
        super.onDestroy();
    }

    private void setLayout(View v) {
        relativeLayout = (RelativeLayout) v.findViewById(R.id.login_birth);
        Drawable drawable = new BitmapDrawable(BitmapTool.resizeBitmap(getActivity(), R.drawable.bgbirth, 0));
        relativeLayout.setBackground(drawable);
        ((Button)v.findViewById(R.id.birthbtn)).setOnClickListener(this);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.birthbtn:
                new DatePickerDialog(getActivity(), this, year, month, day).show();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int onYear, int onMonth, int onDay) {
        ((SignupActivity)getActivity()).setInfoBundle("birth", String.valueOf(onYear) + String.valueOf(onMonth) + String.valueOf(onDay));
        ((SignupActivity)getActivity()).setPage(3);
    }
}
