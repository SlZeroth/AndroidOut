package com.pjcstudio.managementpoop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 7. 13..
 */
public class SetRecodePoopFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setrecord_poop, container, false);
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {
        
    }
}
