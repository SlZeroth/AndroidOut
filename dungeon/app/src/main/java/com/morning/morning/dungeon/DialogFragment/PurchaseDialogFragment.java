package com.morning.morning.dungeon.DialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.morning.morning.dungeon.R;

/**
 * Created by juchan on 2015. 12. 5..
 */
public class PurchaseDialogFragment extends DialogFragment {

    private LinearLayout purchaseLinear;

    public static PurchaseDialogFragment newInstance() {
        PurchaseDialogFragment frag = new PurchaseDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment_purchase, container, false);
        init(rootView);
        return rootView;
    }

    private void init(View v) {
        purchaseLinear = (LinearLayout) v.findViewById(R.id.purchase_linear);
    }
}
