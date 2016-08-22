package com.taeyangsangsa.pickupusedoil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taeyangsangsa.pickupusedoil.Tools.NetworkService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class WriteSugoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button CompleteBtn;
    private SugoModel mInputModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_sugo);


        init();
    }

    void init() {
        CompleteBtn = (Button) findViewById(R.id.sugo_complete);
        CompleteBtn.setOnClickListener(this);
        mInputModel = new SugoModel();
    }

    void sendSugo() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://bizhyper.ddns.net:9235")
                .build();

        NetworkService service = restAdapter.create(NetworkService.class);
        service.Update_SugoData(mInputModel.companyName, mInputModel.companyPhone, mInputModel.address, mInputModel.date, mInputModel.amount, mInputModel.memo, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast.makeText(WriteSugoActivity.this, "등록완료", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                progressDialog.dismiss();
            }
        });

    }

    void bindSugo(SugoModel model) {

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        model.companyName   = " ";
        model.companyPhone  = telManager.getLine1Number();
        model.address       = ((EditText) findViewById(R.id.sugoaddress)).getText().toString();
        model.date          = ((EditText) findViewById(R.id.sugodate)).getText().toString();
        model.amount        = ((EditText) findViewById(R.id.sugofucknum)).getText().toString();
        model.memo          = ((EditText) findViewById(R.id.sugomemo)).getText().toString();
    }
/*
    void searchSugo() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://bizhyper.ddns.net:9235")
                .build();

        NetworkService service = restAdapter.create(NetworkService.class);
        service.Json_Sugomap(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    ArrayList<String> arrayItems = new ArrayList<String>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonItem = jsonArray.getJSONObject(i);
                        String companyName = jsonItem.getString("as_wr_7");
                        String companyPhone = jsonItem.getString("as_wr_8");

                        arrayItems.add(companyName + " / " + companyPhone);
                    }

                    if (arrayItems.size() >= 1) {

                        final String[] items = arrayItems.toArray(new String[arrayItems.size()]);

                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteSugoActivity.this);

                        builder.setTitle("업체를 선택하세요.")        // 제목 설정
                                .setItems(items, new DialogInterface.OnClickListener() {    // 목록 클릭시 설정
                                    public void onClick(DialogInterface dialog, int index) {
                                        ((EditText) findViewById(R.id.edit_sugocompany)).setText(items[index].split(" / ")[0]);
                                        ((EditText) findViewById(R.id.sugophone)).setText(items[index].split(" / ")[1]);
                                    }
                                });

                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();    // 알림창 띄우기

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });

    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id)
        {
            case R.id.sugo_complete:
                bindSugo(mInputModel);
                if(mInputModel.checkNull() == 1) {
                    sendSugo();
                } else {
                    Log.d("AAAA", "0");
                }
                break;
        }
    }

    private class SugoModel {
        public String companyName = "";
        public String companyPhone = "";
        public String address = "";
        public String date = "";
        public String amount = "";
        public String memo = "";

        public int checkNull() {

            if(companyName == "" || companyPhone == "" || address == "" || date == "" || amount == "" || memo == "") {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
