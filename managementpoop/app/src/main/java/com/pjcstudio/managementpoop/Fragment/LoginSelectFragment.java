package com.pjcstudio.managementpoop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.pjcstudio.managementpoop.Activity.DefaultLoginActivity;
import com.pjcstudio.managementpoop.Activity.LoginActivity;
import com.pjcstudio.managementpoop.R;

/**
 * Created by juchanpark on 2015. 7. 10..
 */
public class LoginSelectFragment extends Fragment {

    private Button btnfb, btnemail, btngoogle;
    private TextView btneamillogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login_select, container, false);
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {
        btnfb = (Button) v.findViewById(R.id.facebookloginbtn);
        btnfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).FacebookLogin();
            }
        });
        btnemail = (Button) v.findViewById(R.id.loginemail);
        btnemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).replaceFragment(0);
            }
        });
        btngoogle = (Button) v.findViewById(R.id.googleloginbtn);
        btngoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).GoogleLogin();
            }
        });
        btneamillogin = (TextView) v.findViewById(R.id.email_login);
        btneamillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DefaultLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
