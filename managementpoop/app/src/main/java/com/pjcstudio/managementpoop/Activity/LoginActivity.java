package com.pjcstudio.managementpoop.Activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
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
import com.google.android.gms.common.*;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.pjcstudio.managementpoop.Fragment.LoginSelectBirthFragment;
import com.pjcstudio.managementpoop.Fragment.LoginSelectFragment;
import com.pjcstudio.managementpoop.Fragment.LoginSelectSexFragment;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;


/*
    로그인 선택하는 부분의 액티비티
    UI 는 프래그먼트에서 사용 LoginSelectFragment
 */
public class LoginActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager callbackManager;
    private ProgressDialog mConnectionProgressDialog;
    private GoogleApiClient mPlusClient;
    private GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private String id, name, birth, sex, location;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private Bundle bdate;
    private FragmentManager fm;


    private GoogleCloudMessaging _gcm;
    private String _regId;

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    static final int USERINFO_COMPLETE = 0;
    static final int USERINFO_NOSEX = 1;
    static final int USERINFO_NOLOCATION = 2;
    static final int USERINFO_NOALL = 3;
    static final int FRAGMENT_SEX = 1;
    static final int FRAGMENT_SIGNUP = 0;
    static final int FRAGMENT_BIRTH = 2;

    static final String LOGIN_SUCCESS = "2";
    static final String LOGIN_SIGNUP = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        // Google Login 객체 초기화
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
        autoLogin();
        setData();
        setLayout();



        initGCM();
    }


    // 푸시 Init
    void initGCM(){
        if (checkPlayServices())
        {
            _gcm = GoogleCloudMessaging.getInstance(this);

            registerInBackground();
        }
        else
        {
            Log.i("loginactivity", "|No valid Google Play Services APK found.|");
        }

    }

    // 푸시 등록
    private void registerInBackground()
    {
        new AsyncTask<Void, Void, String>()
        {
            @Override
            protected String doInBackground(Void... params)
            {
                String msg = "";
                try
                {
                    if (_gcm == null)
                    {
                        _gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    _regId = _gcm.register(getString(R.string.gcm_defaultSenderId));
                    msg = "Device registered, registration ID=" + _regId;

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the regID - no need to register again.
//                    storeRegistrationId(_regId);
                    RestAdapter restAdapter = new RestAdapter.Builder()
                            .setEndpoint(ServerInfo.LOGINADDR)
                            .setClient(new OkClient(new OkHttpClient()))
                            .build();
                    NetworkService service = restAdapter.create(NetworkService.class);
                    service.registerpush(_regId,
                            new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {
                                    Log.d("loginactivity","success : "+new String(((TypedByteArray) response.getBody()).getBytes()));
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.d("loginactivity","fail ");
                                }
                            });
                }
                catch (IOException ex)
                {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    Log.d("loginactivity",msg);
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg)
            {
                Log.i("MainActivity", "|" + msg + "|");
            }
        }.execute(null, null, null);
    }

    // google play service가 사용가능한가
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i("MainActivity.java | checkPlayService", "|This device is not supported.|");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // 자동로그인 여부 체크후 이동
    private void autoLogin() {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        String logintype = pref.getString("logintype", ""); // 1 : SNS LOGIN , 2 : DEFAULT LOGIN
        String session = pref.getString("session", "");
        String autoLogin = pref.getString("autologin", "");
        if(autoLogin.length() != 0) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("session", session);
            startActivity(intent);
            finish();
        }
    }

    private void setData() {
        bdate = new Bundle();
    }

    /* 계정정보입력 프래그먼트에서 호출 */
    public void addAccountinfo(Bundle bun) {
        ArrayList<String> Keylist = new ArrayList<String>(Arrays.asList("id", "pw", "email", "name", "birth", "location", "sex"));
        for(int i=0;i<Keylist.size();i++) {
            if(bun.getString(Keylist.get(i)) != null) {
                bdate.putString(Keylist.get(i), bun.getString(Keylist.get(i)));
            }
        }
        Log.i("BundleData", bdate.toString());
    }

    // Fragment Attach
    private void setLayout() {
        fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.headlines_fragment, new LoginSelectFragment());
        fragmentTransaction.commit();
    }

    // 구글로그인 버튼을 클릭했을때 호출
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

    /* 페이스북 로그인콜백 */
    public void FacebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResults) {
                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject Userobject, GraphResponse response) {
                                        Log.i("info", Userobject.toString());
                                        int state = checkFacebookJsonObject(Userobject);
                                        if(Userobject.has("gender") && Userobject.has("location") && Userobject.has("birth")) {
                                            SignupProcess();
                                        } else {
                                            SignupProcess();
                                        }
                                    }
                                });
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException e) {
                    }
                });
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("public_profile", "user_photos", "email", "user_birthday", "user_friends"));
    }

    public void replaceFragment(int Fragmentname) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if(Fragmentname == FRAGMENT_SEX) {
            fragmentTransaction.replace(R.id.headlines_fragment, new LoginSelectSexFragment());
            fragmentTransaction.addToBackStack(null);
        }
        else if(Fragmentname == FRAGMENT_SIGNUP) {
            /*
            fragmentTransaction.replace(R.id.headlines_fragment, new LoginSignupFragment());
            fragmentTransaction.addToBackStack(null);
            */
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        }
        else if(Fragmentname == FRAGMENT_BIRTH) {
            fragmentTransaction.replace(R.id.headlines_fragment, new LoginSelectBirthFragment());
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    /* 페이스북으로 로그인하기 */
    private void SignupProcess() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.LOGINADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        service.SignUpFacebook(bdate.getString("id"), bdate.getString("name"), "19960825", "m", "1111", "1111", new Callback<Response>() {
            @Override
            public void success(Response o, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    if (jsonObject.getString("code").equals(LOGIN_SUCCESS) || jsonObject.getString("code").equals(LOGIN_SIGNUP)) {
                        String session = new JSONObject(jsonObject.getString("info")).getString("session");
                        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("logintype", "1");
                        editor.putString("session", session);
                        editor.putString("autologin", "true");
                        editor.putString("push", "true");
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("session", session);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "로그인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.e("LoginActivity","error : "+error.getMessage());
            }
        });
    }


    private String retnCodeJsonObject(String req) {
        try {
            JSONObject jsonObject = new JSONObject(req);
            String code = jsonObject.getString("code");
            return code;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int checkFacebookJsonObject(JSONObject obj) {
        int retnvalue = USERINFO_COMPLETE; // 기본 성별없음 설정
        int gender_flag = 0;
        int location_flag = 0;

        try {
            bdate.putString("id", obj.getString("id"));
            bdate.putString("name", obj.getString("name"));

            if(obj.has("gender") || obj.has("location") || obj.has("birth")) {
                if(obj.has("gender")) {
                    if(obj.getString("gender").equals("male")) {
                        bdate.putString("sex", "m");
                    } else if(obj.getString("gender").equals("woman")) {
                        bdate.putString("sex", "w");
                    }
                    gender_flag = 1;
                    retnvalue = USERINFO_NOLOCATION;
                }

                if(obj.has("location")) {
                    bdate.putString("location", new JSONObject(obj.getString("location")).getString("name"));
                    location_flag = 1;

                    if(gender_flag == 1) {
                        retnvalue = USERINFO_COMPLETE;
                    } else {
                        retnvalue = USERINFO_NOSEX;
                    }
                }

                if(obj.has("birth")) {
                    bdate.putString("birth", obj.getString("birth"));
                    retnvalue = 3;
                }
            } else {
                retnvalue = USERINFO_NOALL;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retnvalue;
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
        Log.d("ㅇㄹ어로일이", "onConnected:" + bundle);
        mConnectionProgressDialog.dismiss();
        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mPlusClient);
        //String accountName = mPlusClient.getAccountName();
        Log.i("GOOD", currentPerson.getName().toString());
        bdate.putString("id", currentPerson.getId().toString());
        bdate.putString("name", currentPerson.getName().toString());
        SignupProcess();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("FAIL", "FAIL");
        if (mConnectionProgressDialog.isShowing()) {
            // 사용자가 이미 로그인 버튼을 클릭했습니다. 해결을 시작합니다.
            // 연결 오류가 발생했습니다. onConnected()가 연결 대화상자를 닫을 때까지
            // 기다립니다.
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (IntentSender.SendIntentException e) {
                    mPlusClient.connect();
                }
            }
        }

        // 사용자가 로그인 버튼을 클릭할 때 활동을 시작할 수 있도록
        // 인텐트를 저장합니다.
        mConnectionResult = result;
    }
}