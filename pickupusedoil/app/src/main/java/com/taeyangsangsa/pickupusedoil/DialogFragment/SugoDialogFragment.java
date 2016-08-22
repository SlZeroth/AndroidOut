package com.taeyangsangsa.pickupusedoil.DialogFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.taeyangsangsa.pickupusedoil.Fragment.BoardFragment;
import com.taeyangsangsa.pickupusedoil.R;

/**
 * Created by juchanpark on 2015. 9. 14..
 */
public class SugoDialogFragment extends DialogFragment implements View.OnClickListener {

    private Button btnYes, btnNo;
    private Bundle arg;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arg = getArguments();
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialogfragment_sugo, container, false);
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {
        btnYes = (Button) v.findViewById(R.id.sugo_yes);
        btnNo = (Button) v.findViewById(R.id.sugo_no);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.sugo_yes:
                ((BoardFragment)getTargetFragment()).sugoYesDialog(arg.getString("itemNum"));
                this.dismiss();
                break;
            case R.id.sugo_no:
                this.dismiss();
                break;
        }
    }
}
