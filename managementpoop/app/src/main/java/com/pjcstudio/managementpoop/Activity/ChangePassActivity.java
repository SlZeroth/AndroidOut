package com.pjcstudio.managementpoop.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pjcstudio.managementpoop.R;
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

public class ChangePassActivity extends FragmentActivity implements View.OnClickListener {

    private Button btn_ok, btn_back;
    private EditText edit1, edit2, edit3; // UI 순서대로 지정
    private String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
        session = pref.getString("session", "");

        btn_ok = (Button) findViewById(R.id.changepass_ok);
        btn_back = (Button) findViewById(R.id.changepass_back);
        btn_ok.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        edit1 = (EditText) findViewById(R.id.chagnepass_edit);
        edit2 = (EditText) findViewById(R.id.chagnepass_edit2);
        edit3 = (EditText) findViewById(R.id.chagnepass_edit3);
    }

    private void ChangePassword(String nowPass, String changePass) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.LOGINADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        service.changePassword(session, nowPass, changePass, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    if(jsonObject.getString("code").equals("1")) {
                        Toast.makeText(ChangePassActivity.this, "비밀번호 변경성공", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if(jsonObject.getString("code").equals("-3")) {
                        Toast.makeText(ChangePassActivity.this, "현재 비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePassActivity.this, "비밀번호 변경실패", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.changepass_ok:
                if(edit2.getText().toString().equals(edit2.getText().toString())) {
                    ChangePassword(edit1.getText().toString(), edit2.getText().toString());
                } else {
                    Toast.makeText(this, "변경할 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.changepass_back:
                finish();
                break;
        }
    }
}
