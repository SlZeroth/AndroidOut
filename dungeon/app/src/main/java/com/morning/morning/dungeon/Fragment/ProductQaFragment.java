package com.morning.morning.dungeon.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by juchan on 2015. 11. 28..
 */
public class ProductQaFragment extends Fragment {

    private TextView rootView;
    private String info;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info = getArguments().getString("info");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = new TextView(getActivity());
        rootView.setText(info);
        return rootView;
    }
}
