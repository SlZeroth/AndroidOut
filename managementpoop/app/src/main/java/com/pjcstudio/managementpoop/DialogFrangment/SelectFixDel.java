package com.pjcstudio.managementpoop.DialogFrangment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 8. 12..
 */
public class SelectFixDel extends DialogFragment implements View.OnClickListener {

    private SelectFixDelListener selectFixDelListener;
    private String strType;
    private String itemType;

    public interface SelectFixDelListener {
        void deleteItem(String type, String id, String day);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialog_delorfix, container, false);
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {
        ((Button)v.findViewById(R.id.setmode_fix)).setOnClickListener(this);
        ((Button)v.findViewById(R.id.setmode_dels)).setOnClickListener(this);
        strType = getArguments().getString("types");
        itemType = getArguments().getString("itemtype");
        Log.d("TYPE", strType);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.setmode_fix:
                SwitchingModeDialog dialog = new SwitchingModeDialog();
                Bundle bundle = new Bundle();
                bundle.putString("mode", strType);
                bundle.putString("edittype", "fix");
                bundle.putString("year", getArguments().getString("year"));
                bundle.putString("month", getArguments().getString("month"));
                bundle.putString("day", getArguments().getString("day"));
                bundle.putString("id", getArguments().getString("id"));
                dialog.setArguments(bundle);
                dialog.setTargetFragment(getTargetFragment(), 0);
                dialog.show(getFragmentManager(), "SETFOOD");
                dismiss();
                break;
            case R.id.setmode_dels:
                selectFixDelListener = (SelectFixDelListener) getTargetFragment();
                selectFixDelListener.deleteItem(strType, getArguments().getString("id"), getArguments().getString("day"));
                dismiss();
                break;
        }
    }
}
