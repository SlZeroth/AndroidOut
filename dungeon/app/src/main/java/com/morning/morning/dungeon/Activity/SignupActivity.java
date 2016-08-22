package com.morning.morning.dungeon.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.ServerInfo;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Networking;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText editId, editPass, editNickname, editTel;
    private RadioButton radioMan, radioWoman;
    private String strYear, strMonth, strDay;
    private String checkWhat;
    private Boolean isBirth = false;

    static final String SEX_MAN = "m";
    static final String SEX_WOMAN = "w";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().setTitle("회원가입");
        init();
    }

    private void init() {
        editId = (EditText) findViewById(R.id.sign_id);
        editPass = (EditText) findViewById(R.id.sign_password);
        editNickname = (EditText) findViewById(R.id.sign_username);
        editTel = (EditText) findViewById(R.id.sign_phone);
        radioMan = (RadioButton) findViewById(R.id.sign_man);
        radioWoman = (RadioButton) findViewById(R.id.sign_woman);
        findViewById(R.id.sign_birth).setOnClickListener(this);
        findViewById(R.id.sign_btn).setOnClickListener(this);

        strYear = null;
        strMonth = null;
        strDay = null;
    }

    private void checkForm() {
        if(!(editId.getText().toString().equals("") &&
                editNickname.getText().toString().equals("") &&
                editPass.getText().toString().equals("") &&
                editTel.getText().toString().equals(""))) {

            if(isBirth) {

                if(radioMan.isChecked() == true || radioWoman.isChecked() == true)
                {
                    if (radioMan.isChecked()) {
                        checkWhat = SEX_MAN;
                    }
                    if (radioWoman.isChecked()) {
                        checkWhat = SEX_WOMAN;
                    }

                    String birth = strYear + "-" + strMonth + "-" + strDay;
                    signProcess(birth, checkWhat);

                } else {
                    Toast.makeText(this, "성별을 선택하세요.", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "생일을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
        }
    }
    private void signProcess(String birth, String sex) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.SERVER_ADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

            final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);
            Networking networking = restAdapter.create(Networking.class);
            networking.createUser(
                    editId.getText().toString(),
                    editPass.getText().toString(),
                    editNickname.getText().toString(),
                    editNickname.getText().toString(),
                    editTel.getText().toString(),
                    sex,
                    birth,
                    "dungeon",
                    new Callback<Response>() {

                        @Override
                        public void success(Response response, Response response2) {
                            progressDialog.dismiss();
                            try {

                                JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));

                                if(jsonObject.getString("responseCode").equals(StateCode.STATE_SUCCESS)) {
                                    Toast.makeText(SignupActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(SignupActivity.this, "서버와의 연결이 문제가 생겼습니다.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id)
        {
            case R.id.sign_birth:
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(SignupActivity.this, this,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH)+1,
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.sign_btn:
                checkForm();
                break;
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        strYear = String.valueOf(year);
        strMonth = String.valueOf(monthOfYear);
        strDay = String.valueOf(dayOfMonth);

        isBirth = true;
    }
}
