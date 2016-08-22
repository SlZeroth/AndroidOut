package com.pjcstudio.managementpoop.DialogFrangment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 8. 6..
 */
public class SelectModeDialog extends DialogFragment implements View.OnClickListener {

    private Button btn_poop, btn_food;
    private Bundle arg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View rootView = inflater.inflate(R.layout.dialog_selectmode, container, false);
        setInit();
        setLayout(rootView);
        return rootView;
    }

    private void setInit() {
        arg = getArguments();
    }

    private void setLayout(View v) {
        btn_poop = (Button) v.findViewById(R.id.setmode_poop);
        btn_food = (Button) v.findViewById(R.id.setmode_food);
        btn_poop.setOnClickListener(this);
        btn_food.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int Vid = view.getId();
        switch (Vid)
        {
            case R.id.setmode_food:
                //SetFoodRecordDialog setFoodRecordDialog = new SetFoodRecordDialog();
                SwitchingModeDialog dialog = new SwitchingModeDialog();
                arg.putString("mode", "food");
                arg.putString("edittype", "new");
                dialog.setArguments(arg);
                dialog.setTargetFragment(getTargetFragment(), 0);
                dialog.show(getFragmentManager(), "SETFOOD");
                dismiss();
                break;
            case R.id.setmode_poop:
                SwitchingModeDialog dialogs = new SwitchingModeDialog();
                arg.putString("mode", "poop");
                arg.putString("edittype", "new");
                dialogs.setArguments(arg);
                dialogs.setTargetFragment(getTargetFragment(), 0);
                dialogs.show(getFragmentManager(), "SETPOOP");
                dismiss();
                break;
        }
    }
}
