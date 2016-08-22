package com.pjcstudio.managementpoop.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 7. 26..
 */
public class nullfragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_null, container, false);
        return rootView;
    }
}
