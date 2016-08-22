package com.pjcstudio.managementpoop.Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.pjcstudio.managementpoop.Activity.SignupActivity;
import com.pjcstudio.managementpoop.Utility.BitmapTool;
import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 7. 10..
 */
public class LoginSelectSexFragment extends Fragment implements View.OnClickListener {

    private LinearLayout relativeLayout;
    private RadioButton radioMan;
    private RadioButton radioWoman;
    private String sex = null; // 선택 X

    static final String MAN = "m";
    static final String WOMAN = "w";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_select_sex, container, false);
        setLayout(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.d("onDestory", "LoginSelectSexFragment");
        relativeLayout.setBackground(null);
        super.onDestroy();
    }

    private void setLayout(View v) {
        relativeLayout = (LinearLayout) v.findViewById(R.id.login_sex);
        Drawable drawable = new BitmapDrawable(BitmapTool.resizeBitmap(getActivity(), R.drawable.bgsex, 0));
        relativeLayout.setBackground(drawable);
        radioMan = (RadioButton) v.findViewById(R.id.rad_man);
        radioWoman = (RadioButton) v.findViewById(R.id.rad_woman);
        radioMan.setOnClickListener(this);
        radioWoman.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rad_man:
                sex = MAN;
                break;
            case R.id.rad_woman:
                sex = WOMAN;
                break;
        }
        ((SignupActivity)getActivity()).setPage(2);
        addToBundle();
    }

    public void addToBundle() {
        ((SignupActivity)getActivity()).setInfoBundle("sex", sex);
    }
}
