package com.pjcstudio.serialcam.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.pjcstudio.serialcam.Activity.MainActivity;
import com.pjcstudio.serialcam.Activity.TestWifiScan;
import com.pjcstudio.serialcam.Activity.WifiActivity;
import com.pjcstudio.serialcam.Activity.WifiSelecterActivity;
import com.pjcstudio.serialcam.R;

/**
 * Created by juchan on 2015. 10. 29..
 */
public class OptionDialogFragment extends DialogFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    public MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("설정");
        View rootView = inflater.inflate(R.layout.dialogfragment_option, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View v) {
        mainActivity = (MainActivity) getActivity();

        v.findViewById(R.id.option_hname).setOnClickListener(this);
        v.findViewById(R.id.option_devicenum).setOnClickListener(this);
        v.findViewById(R.id.vcuip).setOnClickListener(this);
        v.findViewById(R.id.option_screen).setOnClickListener(this);
        v.findViewById(R.id.comip).setOnClickListener(this);
        v.findViewById(R.id.out).setOnClickListener(this);
        v.findViewById(R.id.option_format).setOnClickListener(this);
        v.findViewById(R.id.option_birth).setOnClickListener(this);
        v.findViewById(R.id.mirring).setOnClickListener(this);
        v.findViewById(R.id.wifiset).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id)
        {
            case R.id.option_hname:
                mainActivity.makeInputDialog("업체이름 입력", "업체이름을 입력하십시오.", mainActivity.DIALOG_COMPANYNAME);
                break;
            case R.id.option_screen:
                mainActivity.makeResolutionChangeDialog();
                break;
            case R.id.option_devicenum:
                mainActivity.makeInputDialog("기기이름 입력", "기기이름을 입력하십시오.", mainActivity.DIALOG_DEVICENAME);
                break;
            case R.id.vcuip:
                mainActivity.makeInputDialog("사진번호 선택", "사진번호를 입력하세요.", mainActivity.DIALOG_PHOTOCOUNTER);
                break;
            case R.id.comip:
                mainActivity.makeInputDialog("아이피 입력", "아이피를 입력하십시오", mainActivity.DIALOG_COMPUTER_IPSET);
                break;
            case R.id.option_birth:
                new DatePickerDialog(getActivity(), this, 2015, 11, 1).show();
                break;
            case R.id.option_format:
                mainActivity.formatDevice();
            case R.id.out:
                dismiss();
                break;
            case R.id.mirring:
                Intent intent = new Intent("com.samsung.wfd.LAUNCH_WFD_PICKER_DLG");
                startActivityForResult(intent, 2);
                break;
            case R.id.wifiset:
                Intent intentWifi = new Intent(getActivity(), TestWifiScan.class);
                startActivity(intentWifi);
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        SharedPreferences pref = getActivity().getSharedPreferences("SAVE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("YEAR", String.valueOf(i));
        editor.putString("MONTH", String.valueOf(i1));
        editor.putString("DAY", String.valueOf(i2));
        editor.commit();
    }
}
