package com.morning.morning.dungeon.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    public String Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.inject(this);

        Code = getIntent().getStringExtra("Code");
        getItem();
    }

    void getItem() {

        Util.getNetworkService().getGameInfosByCategoryCode(Code, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {

                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);
                            JSONObject thumbnailObject = new JSONObject(jsonObj.getString("thumbnailFileJSONObject"));
                            Log.d("kkk", thumbnailObject.getString("fileUri"));
                        }
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

    public void start(String name) {
        Intent intent = new Intent(this, CategoryGamesActivity.class);
        intent.putExtra("Category", name);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id)
        {
            case R.id.rpg:
                start("rpg");
                break;
        }
    }
}
