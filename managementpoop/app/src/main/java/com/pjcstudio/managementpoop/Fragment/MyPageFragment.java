package com.pjcstudio.managementpoop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pjcstudio.managementpoop.Activity.MainActivity;
import com.pjcstudio.managementpoop.Activity.SettingActivity;
import com.pjcstudio.managementpoop.DialogFrangment.MyInfoDialogFragment;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.pjcstudio.managementpoop.Views.SquareHeightButton;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 8. 8..
 */
public class MyPageFragment extends Fragment implements View.OnClickListener {

    private TextView textLevel, textRecommend, textLevelName, textLevelComment;
    private ImageButton setting;
    private SquareHeightButton levelButton;
    private String session;
    private String recommendStr;
    private String levelName, levelComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mypage, container, false);
        setLayout(rootView);
        setLevel();
        return rootView;
    }

    private void setLayout(View v) {
        session = ((MainActivity)getActivity()).session;
        levelButton = (SquareHeightButton) v.findViewById(R.id.mylevelimage);
        levelButton.setOnClickListener(this);
        textLevel = (TextView) v.findViewById(R.id.mypage_level);
        textRecommend = (TextView) v.findViewById(R.id.textview_tip);
        textLevelName = (TextView) v.findViewById(R.id.mypage_levelname);
        textLevelComment = (TextView) v.findViewById(R.id.mypage_levelcomment);
        setting = (ImageButton) v.findViewById(R.id.settingbtn);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLevel() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3 * 1000, TimeUnit.MILLISECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(okHttpClient))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        service.getLevel(session, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("MyPage", jsonObject.toString());
                    if(jsonObject.getString("code").equals("1")) {
                        int level = Integer.parseInt(jsonObject.getString("level"));
                        getLevelString(level);
                        textLevel.setText("LV. " + jsonObject.getString("level"));
                        textLevelName.setText(levelName);
                        textLevelComment.setText(levelComment);
                        getRecommend();
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

    private void getRecommend() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(3 * 1000, TimeUnit.MILLISECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(okHttpClient))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        service.getRecommend(session, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("recommand", jsonObject.toString());
                    Log.d("MyPages", jsonObject.toString());
                    if(jsonObject.getString("code").equals("1")) {
                        recommendStr = jsonObject.getString("recommend");
                        textRecommend.setText(recommendStr);
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
    }

    private void getLevelString(int level) {
        switch (level)
        {
            case 0:
                levelComment = "더 많은 응가를 위해 분발하세요!";
                levelName = "응가의 노예";
                levelButton.setBackgroundResource(R.drawable.lv1);
                break;
            case 1:
                levelComment = "나도 조금만 신경 쓰면 잘 먹고 잘 쌀 수 있다!";
                levelName = "응가의 백성";
                levelButton.setBackgroundResource(R.drawable.lv2);
                break;
            case 2:
                levelComment = "이제 응가는 당신의 것.. 지금 잡은 응가 놓치지 마세요.";
                levelName = "응가의 주인";
                levelButton.setBackgroundResource(R.drawable.lv3);
                break;
            case 3:
                levelComment = "날 때부터 잘 먹고 잘 싸는 당신이 최고!";
                levelName = "응가의 제왕";
                levelButton.setBackgroundResource(R.drawable.lv4);
                break;
            case 4:
                levelComment = "당신의 응가 빈도는 너무 높아요. 본인이 응가인가요?";
                levelName = "내가 곧 응가";
                levelButton.setBackgroundResource(R.drawable.lv5);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.mylevelimage:
                MyInfoDialogFragment myInfoDialogFragment = new MyInfoDialogFragment();
                myInfoDialogFragment.show(getChildFragmentManager(), "MYINFO");
                break;
        }
    }
}
