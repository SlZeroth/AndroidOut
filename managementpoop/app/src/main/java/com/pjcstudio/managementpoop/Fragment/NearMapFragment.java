package com.pjcstudio.managementpoop.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pjcstudio.managementpoop.Activity.MainActivity;
import com.pjcstudio.managementpoop.Activity.SettingActivity;
import com.pjcstudio.managementpoop.Adapter.MapAdapter;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 7. 7..
 * 자기주변 화장실찾는 프래그먼트
 */
public class NearMapFragment extends Fragment implements LocationListener, View.OnClickListener {

    private GoogleMap googleMap;
    private Button nearMapBtn;
    private EditText editNear;
    private ListView listArea;
    private MapAdapter adapter;
    private ArrayList<String> listitem;
    private String session;
    private MapView map;
    private LocationManager locationManager;
    private double searchLat, searchLng;
    private int flag_ifsearch;
    private Bitmap markerimage, markerimage2;
    private int setMyPos = 0;
    private int setOpenFirst = 0;
    private int delay = 5;
    private ProgressDialog progressDialog;

    Boolean useAble = false;

    static final int ERRORCODE_NOTTOILET = -3;

    GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Log.d("onMyLocationChage", "Called");
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            if(setMyPos == 0) {
                CameraUpdate center=
                        CameraUpdateFactory.newLatLng(loc);
                CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
                googleMap.moveCamera(center);
                googleMap.animateCamera(zoom);
                setMyPos =1;
                progressDialog.dismiss();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nearmap, container, false);
        setInit();
        setLayout(rootView, savedInstanceState);
        return rootView;
    }

    private void setLayout(View v, Bundle save) {
        nearMapBtn = (Button) v.findViewById(R.id.nearmapbtn);
        nearMapBtn.setOnClickListener(this);
        editNear = (EditText) v.findViewById(R.id.nearmapedit);
        ((ImageButton) v.findViewById(R.id.settingbtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
        listArea = (ListView) v.findViewById(R.id.list_map);
        adapter = new MapAdapter(getActivity());
        listArea.setAdapter(adapter);
        listitem = new ArrayList<String>();
        map = (MapView) v.findViewById(R.id.map);
        map.onCreate(save);
        googleMap = map.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);


        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {

                if(isGpsOn()) {
                    Location location = googleMap.getMyLocation();
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    if (loc == null) {
                        Toast.makeText(getActivity(), "위치정보를 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        getNearBath(String.valueOf(loc.latitude), String.valueOf(loc.longitude), 1);
                    }
                }
                return false;
            }
        });


        markerimage = resizeMapIcons(85,116, R.drawable.rsz_pin);
        markerimage2 = resizeMapIcons(85,116, R.drawable.yello_pin);
        editNear.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == keyEvent.KEYCODE_ENTER && keyEvent.getAction() == keyEvent.ACTION_DOWN) {
                    searchInEditText();
                }
                return false;
            }
        });

        MapsInitializer.initialize(this.getActivity());
        /*
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(37.5651,
                        126.98955));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15); */



        chkGpsService();
        //locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, this);
    }

    private void setInit() {
        session = ((MainActivity)getActivity()).session;
        setMyPos =0;
        setOpenFirst = 0;
        flag_ifsearch = 0;
        delay = 5;
    }

    private void getNearBath(String lat, String lng, final int type) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);

        progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);

        service.getToilet(session, lat, lng, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("NearMap", jsonObject.toString());
                    if(jsonObject.getString("code").equals("1")) {
                        if(type == 0) {
                            listitem.clear();
                        }
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("toilets"));
                        for(int idx=0;idx<jsonArray.length();idx++) {
                            JSONObject jsonSub = jsonArray.getJSONObject(idx);
                            String lat = jsonSub.getString("LAT");
                            String lng = jsonSub.getString("LNG");
                            Log.d("LATLNG", lat + lng);
                            String address = findAddress(Double.parseDouble(lat), Double.parseDouble(lng));
                            Log.d("ADDRESS", address);
                            LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 0);

                            MarkerOptions marker = new MarkerOptions().position(latLng);
                            if(type == 1) { // 자동위치
                                address = address + " 에 있어요!";
                                listitem.add(address);
                                adapter.setListItem(listitem);
                                marker.icon(BitmapDescriptorFactory.fromBitmap(markerimage2));
                            } else { // 검색버튼
                                address = address + " 에 있어요!";
                                listitem.add(address);
                                adapter.setListItem(listitem);
                                marker.icon(BitmapDescriptorFactory.fromBitmap(markerimage));
                            }
                            marker.title(address);
                            googleMap.addMarker(marker);
                        }
                        adapter.notifyDataSetChanged();
                    } else if(jsonObject.getString("code").equals("-3")) {
                        Toast.makeText(getActivity(), "반경 500m내 화장실이 없습니다. 검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
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

    public Bitmap resizeMapIcons(int width, int height, int resid){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),resid);
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private String findAddress(double lat, double lng) {
        StringBuffer bf = new StringBuffer();
        Geocoder geocoder = new Geocoder(getActivity(), Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
                address = geocoder.getFromLocation(lat, lng, 1);
                // 설정한 데이터로 주소가 리턴된 데이터가 있으면
                if (address != null && address.size() > 0) {
                    // 주소
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();

                    // 전송할 주소 데이터 (위도/경도 포함 편집)
                    bf.append(currentLocationAddress);
                }
            }

        } catch (IOException e) {
            Toast.makeText(getActivity(), "주소취득 실패"
                    , Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return bf.toString();
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("NearMapFragment", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        googleMap.setOnMyLocationChangeListener(null);
    }

    @Override
    public void onDestroy() {
        Log.d("NearMapFragment", "onDestroy");
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    public void onLocationChanged(Location location) {
        final double lat = location.getLatitude();
        final double lnt = location.getLongitude();
        //googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lnt)));
        if(flag_ifsearch == 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lnt)));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            getNearBath(String.valueOf(lat), String.valueOf(lnt), 1);
        }
        //Log.d("ADDRESS", findAddress(lat, lnt));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.nearmapbtn:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                flag_ifsearch = 1;
                if(editNear.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "위치를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    searchInEditText();
                }
                break;
        }
    }

    private void searchInEditText() {
        LatLng latLng = getLocationFromAddress(editNear.getText().toString());
        Log.d("Point", searchLat + " : " + searchLng);
        if(latLng != null) {
            getNearBath(String.valueOf(String.valueOf(latLng.latitude)), String.valueOf(latLng.longitude), 0);
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        } else {
            Toast.makeText(getActivity(), "찾으시는 위치가 없습니다.", Toast.LENGTH_SHORT).show();
        }
        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(searchLat,searchLng)));

    }

    private Boolean isGpsOn() {
        String gps = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {
            useAble = false;
            Toast.makeText(getActivity(), "GPS 를 작동해야 위치를 찾을수있습니다!", Toast.LENGTH_SHORT).show();
        } else {
            useAble = true;
        }

        return useAble;
    }

    private boolean chkGpsService() {

        String gps = android.provider.Settings.Secure.getString(getActivity().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        Log.d(gps, "aaaa");

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(getActivity());
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                    progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
                    googleMap.setOnMyLocationChangeListener(onMyLocationChangeListener);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                        }
                    }, 3000);
                }
            })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
                            googleMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                }
                            }, 3000);
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
            googleMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
            return true;
        }
    }


}
