package com.pjcstudio.managementpoop.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.BitmapTool;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class DefaultLoginActivity extends FragmentActivity {

    private EditText editId, editPw;
    private LinearLayout linDefualt;
    private Button btnLogin;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_login);
        setLayout();

        linDefualt = (LinearLayout) findViewById(R.id.lindefault);
        Drawable drawable = new BitmapDrawable(BitmapTool.resizeBitmap(this, R.drawable.signbg, 1));
        linDefualt.setBackground(drawable);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String saveFlag = pref.getString("saveid", "");
        if(saveFlag.equals("true")) {
            editId.setText(pref.getString("myid", ""));
        }
    }

    private void setLayout() {
        editId = (EditText) findViewById(R.id.login_id);
        editPw = (EditText) findViewById(R.id.login_pw);
        checkBox = (CheckBox) findViewById(R.id.login_check);

        btnLogin = (Button) findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editId.getText().toString().equals("") ||
                        editPw.getText().toString().equals("")) {
                    Toast.makeText(DefaultLoginActivity.this, "아이디 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(ServerInfo.LOGINADDR)
                            .setClient(new OkClient(new OkHttpClient()))
                            .build();
                    NetworkService service = restAdapter.create(NetworkService.class);
                    service.Login(editId.getText().toString(), editPw.getText().toString(), new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            try {

                                JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                                Log.d("Login", jsonObject.toString());
                                if(jsonObject.getString("code").equals("1")) {
                                    String session = new JSONObject(jsonObject.getString("info")).getString("session");
                                    SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putString("myid", editId.getText().toString()); // 일반 LOGIN
                                    if(checkBox.isChecked()) {
                                        editor.putString("saveid", "true");
                                    } else {
                                        editor.putString("saveid", "");
                                    }
                                    //editor.putString("session", session);
                                    editor.commit();
                                    Intent intent = new Intent(DefaultLoginActivity.this, MainActivity.class);
                                    intent.putExtra("session", session);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(DefaultLoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }
        });
    }
}
