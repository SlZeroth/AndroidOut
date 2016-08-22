package com.pjcstudio.managementpoop.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pjcstudio.managementpoop.Activity.SignupActivity;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.BitmapTool;

/**
 * Created by juchanpark on 2015. 8. 12..
 */
public class LoginSelectLocationFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout relativeLayout;
    private String locationStr = null;

    public interface LoginSelectLocationListener {
        void setLocation(String location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_select_location, container, false);
        setLayout(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.d("onDestory", "LoginSelectLocationFragment");
        relativeLayout.setBackground(null);
        super.onDestroy();
    }

    private void setLayout(View v) {
        ((Button)v.findViewById(R.id.btn_location)).setOnClickListener(this);
        relativeLayout = (RelativeLayout) v.findViewById(R.id.login_location);
        Drawable drawable = new BitmapDrawable(BitmapTool.resizeBitmap(getActivity(), R.drawable.bglocation, 1));
        relativeLayout.setBackground(drawable);
    }

    public void addToBundle() {
        ((SignupActivity)getActivity()).setInfoBundle("location", locationStr);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.btn_location:
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());
                builder.setTitle("지역선택");

                final CharSequence[] choiceList =
                        {"서울", "부산" , "대구" , "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북",
                            "전남", "경북", "경남", "제주", "해외", "세종"};

                int selected = 0; // select at 0


                builder.setSingleChoiceItems(
                        choiceList,
                        selected,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                Toast.makeText(
                                        getActivity(),
                                        "Select " + choiceList[which],
                                        Toast.LENGTH_SHORT
                                )
                                        .show();
                                locationStr = (String) choiceList[which];
                                dialog.dismiss();
                                addToBundle();
                                ((SignupActivity)getActivity()).SignProcess();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }
    }
}
