package com.morning.morning.dungeon.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.ServerInfo;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.NetworkUtility;
import com.morning.morning.dungeon.Utility.Networking;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private Button fbBtn, googleBtn, naverBtn;

    private GoogleApiClient mPlusClient;
    private ConnectionResult mConnectionResult;
    private ProgressDialog mConnectionProgressDialog;
    private CallbackManager callbackManager;

    static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    static final String LOGIN_FACEBOOK = "facebook";
    static final String LOGIN_GOOGLE = "google";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());

        check();

        mPlusClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");
        callbackManager = CallbackManager.Factory.create();

        init();
    }

    private void init() {
        findViewById(R.id.login_default).setOnClickListener(this);
        googleBtn = (Button) findViewById(R.id.btn_googlelogin);
        fbBtn = (Button) findViewById(R.id.btn_facebooklogin);
        googleBtn.setOnClickListener(this);
        fbBtn.setOnClickListener(this);
    }

    /* 자동로그인 가능여부 체크 */
    private void check() {
        SharedPreferences pref = getSharedPreferences("SAVE", MODE_PRIVATE);
        if(!(pref.getString("session", "").equals(""))) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void Login(final String id, final String name, final String birth, final String type) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.SERVER_ADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        Networking networking = restAdapter.create(Networking.class);
        Log.d("LOGIN", id);
        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "로그인 중 입니다..", true);

        networking.login(id, "1", type, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    Log.d("dfd", "fdklfsfd");
                    progressDialog.dismiss();
                    //String session = NetworkUtility.getHeaderData(response2.getHeaders(), "session");
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                    Log.d("json", jsonObject.toString());

                    if (jsonObject.getString("code").equals(StateCode.STATE_SUCCESS)) {

                        SharedPreferences.Editor editor = getSharedPreferences("SAVE", MODE_PRIVATE).edit();
                        //editor.putString("session", session);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else if (jsonObject.getString("code").equals("-10003")) {

                        Log.d("signUpSNS", "GO");
                        signUpSNS(id, name, birth, type);

                    } else {
                        Toast.makeText(LoginActivity.this, "로그인 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });
    }

    private void signUpSNS(String id, String name, String birth, String type) {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.SERVER_ADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "로그인 중 입니다..", true);
        Networking networking = restAdapter.create(Networking.class);
        networking.createUser(id, "", name, name, "", "", birth, type, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));

                    if(jsonObject.getString("responseCode").equals(StateCode.STATE_SUCCESS)) {
                        Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void FacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        final GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject Userobject, GraphResponse response) {
                                        try {
                                            Login(Userobject.getString("id"), "", "", LOGIN_FACEBOOK);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, "페이스북 로그인을 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("public_profile", "user_photos", "email", "user_birthday", "user_friends"));
    }

    public void GoogleLogin() {
        mPlusClient.connect();
        if (mConnectionResult == null) {
            mConnectionProgressDialog.show();
        } else {
            try {
                mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                // 다시 연결을 시도합니다.
                mConnectionResult = null;
                mPlusClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && resultCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("onConnected", "onConnected : " + bundle);
        mConnectionProgressDialog.dismiss();
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mPlusClient);
        Login(currentPerson.getId().toString(), currentPerson.getName().toString(), currentPerson.getBirthday().toString(), LOGIN_GOOGLE);
        Log.d("ID", currentPerson.getId().toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mConnectionProgressDialog.isShowing()) {
            // 사용자가 이미 로그인 버튼을 클릭했습니다. 해결을 시작합니다.
            // 연결 오류가 발생했습니다. onConnected()가 연결 대화상자를 닫을 때까지
            // 기다립니다.
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.connect();
                }
            }
        }

        // 사용자가 로그인 버튼을 클릭할 때 활동을 시작할 수 있도록
        // 인텐트를 저장합니다.
        mConnectionResult = connectionResult;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.btn_googlelogin:
                GoogleLogin();
                break;
            case R.id.btn_facebooklogin:
                FacebookLogin();
                break;
            case R.id.login_default:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        }
    }
}
