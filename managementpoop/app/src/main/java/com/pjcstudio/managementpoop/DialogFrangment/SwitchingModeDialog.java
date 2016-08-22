package com.pjcstudio.managementpoop.DialogFrangment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.pjcstudio.managementpoop.Fragment.FoodRecord;
import com.pjcstudio.managementpoop.Fragment.PoopRecord;
import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 8. 15..
 */
public class SwitchingModeDialog extends DialogFragment implements View.OnClickListener {

    private FragmentManager fm;
    private Fragment fragmentFood, fragmentPoop;
    private Button mBtnFood, mBtnPoop;
    private Bundle arg;
    private int nowTab;
    private int editmode;

    static final int TAB_POOP = 0;
    static final int TAB_FOOD = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        arg = getArguments();
        View rootView = inflater.inflate(R.layout.dialog_switching, container, false);
        setLayout(rootView);
        setFragment();
        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.d("Switching", "onDestroy");

        super.onDestroy();
    }

    private void setLayout(View v) {
        fragmentFood = null;
        fragmentPoop = null;
        fm = getChildFragmentManager();
        mBtnFood = (Button) v.findViewById(R.id.diatab_food);
        mBtnPoop = (Button) v.findViewById(R.id.diatab_poop);
        if(arg.getString("edittype").equals("new")) {
            mBtnFood.setOnClickListener(this);
            mBtnPoop.setOnClickListener(this);
        }
    }

    private void setFragment() {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(arg.getString("mode").equals("poop")) {
            Fragment poopFragment = new PoopRecord();
            poopFragment.setArguments(arg);
            poopFragment.setTargetFragment(getTargetFragment(), 0);
            Log.d("arg", arg.toString());
            fragmentTransaction.replace(R.id.switching_fragment, poopFragment);
            nowTab = TAB_POOP;
        }
        else if(arg.getString("mode").equals("food")) {
            mBtnFood.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            mBtnFood.setTextColor(Color.parseColor("#4CBACB"));
            mBtnPoop.setBackgroundColor(Color.parseColor("#626262"));
            mBtnPoop.setTextColor(Color.parseColor("#A9A9A9"));
            Fragment foodFragment = new FoodRecord();
            foodFragment.setArguments(arg);
            foodFragment.setTargetFragment(getTargetFragment(), 0);
            fragmentTransaction.replace(R.id.switching_fragment, foodFragment);
            nowTab = TAB_FOOD;
        }
        fragmentTransaction.commit();
    }

    private void setButtonReset() {
        mBtnFood.setBackgroundColor(Color.parseColor("#626262"));
        mBtnFood.setTextColor(Color.parseColor("#A9A9A9"));
        mBtnPoop.setBackgroundColor(Color.parseColor("#626262"));
        mBtnPoop.setTextColor(Color.parseColor("#A9A9A9"));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        setButtonReset();
        switch (id)
        {
            case R.id.diatab_food:
                if(nowTab == TAB_POOP) {
                    mBtnFood.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    mBtnFood.setTextColor(Color.parseColor("#4CBACB"));
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Fragment foodFragment = new FoodRecord();
                    foodFragment.setArguments(arg);
                    foodFragment.setTargetFragment(getTargetFragment(), 0);
                    foodFragment.setArguments(arg);
                    fragmentTransaction.replace(R.id.switching_fragment, foodFragment);
                    fragmentTransaction.commit();
                    nowTab = TAB_FOOD;
                }
                break;
            case R.id.diatab_poop:
                if(nowTab == TAB_FOOD) {
                    mBtnPoop.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    mBtnPoop.setTextColor(Color.parseColor("#4CBACB"));
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Fragment poopFragment = new PoopRecord();
                    poopFragment.setArguments(arg);
                    poopFragment.setTargetFragment(getTargetFragment(), 0);
                    poopFragment.setArguments(arg);
                    fragmentTransaction.replace(R.id.switching_fragment, poopFragment);
                    fragmentTransaction.commit();
                    nowTab = TAB_POOP;
                }
                break;
        }
    }
}
