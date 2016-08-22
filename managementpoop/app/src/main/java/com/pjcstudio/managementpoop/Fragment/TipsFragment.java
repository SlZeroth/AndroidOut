package com.pjcstudio.managementpoop.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.pjcstudio.managementpoop.Activity.SettingActivity;
import com.pjcstudio.managementpoop.Activity.WebViewActivity;
import com.pjcstudio.managementpoop.Adapter.SlideAdapter;
import com.pjcstudio.managementpoop.Adapter.TipsAdapter;
import com.pjcstudio.managementpoop.Items.TipsItem;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.pjcstudio.managementpoop.Views.ScrollableGridView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 8. 5..
 */
public class TipsFragment extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener{

    private Button menu_tip, menu_dis, menu_food, menu_event;
    private Fragment app;
    private SliderLayout adapterViewFlipper;
    private ImageButton btn_sett;
    private ScrollableGridView gridTips;
    private TipsAdapter tipsAdapter;
    private SlideAdapter slideAdapter;
    private ArrayList<TipsItem> itemlist;
    private ArrayList<String> arr_Link;
    private int clickbtn = 1;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            slideAdapter.addItem(bitmap);
            Log.d("onBitmapLoad", "Load");
            int count = slideAdapter.getCount();
            Log.d("count", "count  : " + count);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.d("eror", "error");
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.d("eroraaaa", "error");
        }
    };

    private static final int MENU_TIPS = 1;
    private static final int MENU_DIS = 2;
    private static final int MENU_FOOD = 3;
    private static final int MENU_EVENT = 4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tips, container, false);
        setLayout(rootView);
        setInit();
        return rootView;
    }

    private void setLayout(View v) {
        app = this;
        arr_Link = new ArrayList<String>();
        itemlist = new ArrayList<TipsItem>();
        adapterViewFlipper = (SliderLayout) v.findViewById(R.id.tip_tipbanner);
        adapterViewFlipper.setPresetTransformer(SliderLayout.Transformer.Accordion);
        adapterViewFlipper.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        adapterViewFlipper.setDuration(4000);
        slideAdapter = new SlideAdapter(getActivity());
        btn_sett = (ImageButton) v.findViewById(R.id.settingbtn);
        menu_tip = (Button) v.findViewById(R.id.tips_tips);
        menu_dis = (Button) v.findViewById(R.id.tips_dis);
        menu_food = (Button) v.findViewById(R.id.tips_food);
        menu_event = (Button) v.findViewById(R.id.tips_event);
        gridTips = (ScrollableGridView) v.findViewById(R.id.gridtip);
        tipsAdapter = new TipsAdapter(getActivity());
        menu_tip.setOnClickListener(this);
        menu_dis.setOnClickListener(this);
        menu_food.setOnClickListener(this);
        menu_event.setOnClickListener(this);
        btn_sett.setOnClickListener(this);
        gridTips.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                TipsItem tipsItem = (TipsItem) itemlist.get(pos);
                String moduleName = null;
                switch (Integer.parseInt(tipsItem.module_num))
                {
                    case 144:
                        moduleName = "knowhow";
                        break;
                    case 147:
                        moduleName = "disease";
                        break;
                    case 149:
                        moduleName = "food";
                        break;
                    case 151:
                        moduleName = "event";
                        break;
                }
                String link = String.format(ServerInfo.WEBURL + "mid=%s" + "&document_srl=%s", moduleName, tipsItem.document_num);
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("link", link);
                startActivity(intent);
            }
        });
    }

    private void setInit() {

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(2 * 1000, TimeUnit.MILLISECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR2)
                .setClient(new OkClient(okHttpClient))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
        service.getBoardData(String.valueOf(clickbtn), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("TipsJSONArray", jsonArray.toString());
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TipsItem tipsItem = new TipsItem();
                        tipsItem.title = jsonObject.getString("title");
                        Log.d("title", tipsItem.title);
                        tipsItem.bglink = ServerInfo.APIBOARD + jsonObject.getString("uploaded_filename");
                        tipsItem.document_num = jsonObject.getString("document_srl");
                        tipsItem.module_num = jsonObject.getString("module_srl");
                        itemlist.add(tipsItem);
                        tipsAdapter.setArrayItem(itemlist);
                        gridTips.setAdapter(tipsAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });

        final ProgressDialog progressDialog2 = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
        service.getBoardBanner(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    for(int idx=0;idx<jsonArray.length();idx++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(idx);
                        String title = jsonObject.getString("title");
                        String bannerLink = ServerInfo.APIBOARD + jsonObject.getString("uploaded_filename");
                        TextSliderView textSliderView = new TextSliderView(getActivity());
                        Bundle bundle = new Bundle();
                        bundle.putString("itemnum", String.valueOf(idx));
                        bundle.putString("module_srl", jsonObject.getString("module_srl"));
                        bundle.putString("document_srl", jsonObject.getString("document_srl"));
                        textSliderView
                                .description(title)
                                .image(bannerLink)
                                .setScaleType(BaseSliderView.ScaleType.Fit)
                                .setOnSliderClickListener((BaseSliderView.OnSliderClickListener) app)
                                .bundle(bundle);
                        adapterViewFlipper.addSlider(textSliderView);
                    }
                    slideAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog2.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog2.dismiss();
            }
        });
    }

    private void onChangeMenu() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(2 * 1000, TimeUnit.MILLISECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR2)
                .setClient(new OkClient(okHttpClient))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        itemlist.clear();
        Log.d("CLICK", String.valueOf(clickbtn));
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
        service.getBoardData(String.valueOf(clickbtn), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("TipsJSONArray", jsonArray.toString());
                    for(int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        TipsItem tipsItem = new TipsItem();
                        tipsItem.title = jsonObject.getString("title");

                        tipsItem.bglink = ServerInfo.APIBOARD + jsonObject.getString("uploaded_filename");
                        tipsItem.document_num = jsonObject.getString("document_srl");
                        tipsItem.module_num = jsonObject.getString("module_srl");
                        itemlist.add(tipsItem);
                        tipsAdapter.setArrayItem(itemlist);
                        tipsAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });
    }

    private void setBackgroundButton() {
        menu_tip.setBackgroundColor(Color.parseColor("#626262"));
        menu_dis.setBackgroundColor(Color.parseColor("#626262"));
        menu_event.setBackgroundColor(Color.parseColor("#626262"));
        menu_food.setBackgroundColor(Color.parseColor("#626262"));
        menu_tip.setTextColor(Color.parseColor("#D8D8D8"));
        menu_dis.setTextColor(Color.parseColor("#D8D8D8"));
        menu_event.setTextColor(Color.parseColor("#D8D8D8"));
        menu_food.setTextColor(Color.parseColor("#D8D8D8"));
    }

    @Override
    public void onClick(View view) {
        int Vid = view.getId();

        if(Vid == R.id.tips_tips || Vid == R.id.tips_dis || Vid == R.id.tips_food || Vid == R.id.tips_event) {
            Button btnss = (Button) view;
            setBackgroundButton();
            view.setBackgroundColor(Color.parseColor("#EEEEEE"));
            btnss.setTextColor(Color.parseColor("#00BFFF"));
        }

        switch (Vid)
        {
            case R.id.tips_tips:
                clickbtn = MENU_TIPS;
                break;
            case R.id.tips_dis:
                clickbtn = MENU_DIS;
                break;
            case R.id.tips_food:
                clickbtn = MENU_FOOD;
                break;
            case R.id.tips_event:
                clickbtn = MENU_EVENT;
                break;
            case R.id.settingbtn:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
        }
        onChangeMenu();
    }

    @Override
    public void onSliderClick(BaseSliderView baseSliderView) {
        Bundle args = baseSliderView.getBundle();
        Log.d("onSliderClick", args.getString("itemnum"));
        String moduleName = "banner";
        String link = String.format(ServerInfo.WEBURL + "mid=%s" + "&document_srl=%s", moduleName, args.getString("document_srl"));
        Log.d("link", link);
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra("link", link);
        startActivity(intent);
    }
}
