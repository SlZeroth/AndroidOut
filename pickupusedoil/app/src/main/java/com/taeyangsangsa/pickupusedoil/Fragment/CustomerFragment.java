package com.taeyangsangsa.pickupusedoil.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taeyangsangsa.pickupusedoil.MainActivity;
import com.taeyangsangsa.pickupusedoil.R;

/**
 * Created by juchanpark on 2015. 8. 27..
 */
public class CustomerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_customer, container, false);
        ((MainActivity)getActivity()).setActionbarTitle(2);
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {

    }
}
