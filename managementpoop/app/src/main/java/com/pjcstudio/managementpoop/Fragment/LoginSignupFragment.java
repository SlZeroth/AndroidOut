package com.pjcstudio.managementpoop.Fragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.pjcstudio.managementpoop.Activity.LoginActivity;
import com.pjcstudio.managementpoop.Activity.SignupActivity;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.BitmapTool;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 7. 9..
 */
public class LoginSignupFragment extends Fragment implements View.OnClickListener {

    private LinearLayout relativeLayout;
    private EditText editId;
    private EditText editPasswd;
    private EditText editMail;
    private CheckBox checkAutologin;
    private Button btnSign;
    private String id;
    private String pw;
    private String email;
    private EditText editText = null;
    private int sameId = 1;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editText = (EditText) view;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        View rootView = inflater.inflate(R.layout.fragment_test3, null);
        setLayout(rootView);


        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return rootView;
    }

    private void setLayout(View v) {

        relativeLayout = (LinearLayout) v.findViewById(R.id.login_signup);
        checkAutologin = (CheckBox) v.findViewById(R.id.sign_autologin);
        checkAutologin.setChecked(true);
        Drawable drawable = new BitmapDrawable(BitmapTool.resizeBitmap(getActivity(), R.drawable.signbg, 1));
        relativeLayout.setBackground(drawable);
        editId = (EditText) v.findViewById(R.id.sign_id);
        editPasswd = (EditText) v.findViewById(R.id.sign_passwd);
        editMail = (EditText) v.findViewById(R.id.sign_email);
        btnSign = (Button) v.findViewById(R.id.sign_btn);
        btnSign.setOnClickListener(this);
        editId.setOnClickListener(onClickListener);
        editPasswd.setOnClickListener(onClickListener);
        editMail.setOnClickListener(onClickListener);
        //editId.requestFocus();
    }

    public void addToBundle() {
        String id = editMail.getText().toString();
        String pw = editPasswd.getText().toString();
        String email = editId.getText().toString();
        ((SignupActivity)getActivity()).setInfoBundle("id", id);
        ((SignupActivity)getActivity()).setInfoBundle("pw", pw);
        ((SignupActivity)getActivity()).setInfoBundle("email", email);
    }

    public int findId(final String ids) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.LOGINADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        service.findId(ids, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("SIGN IDCHECK", jsonObject.toString());
                    if(jsonObject.getString("code").equals("2")) {
                        Toast.makeText(getActivity(), "아이디가 중복되었습니다.", Toast.LENGTH_SHORT).show();
                    } else if(jsonObject.getString("code").equals("1")) {
                        ((SignupActivity)getActivity()).setPage(1); // GOTO SEX FRAGMENT
                        addToBundle();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        return 1;
    }

    @Override
    public void onDestroy() {
        Log.d("onDestory", "LoginSignupFragment");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.sign_btn:
                if(editId.getText().toString().equals("") || editPasswd.getText().toString().equals("") || editMail.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "빈칸을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(editText != null) {
                        if (imm.isAcceptingText()) {
                            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                        }
                    }
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    if(editPasswd.getText().toString().length() >= 8 ) {
                        findId(editId.getText().toString());
                    } else {
                        Toast.makeText(getActivity(), "비밀번호를 8글자 이상입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
