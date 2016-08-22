package com.morning.morning.dungeon.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.morning.morning.dungeon.Adapter.AmountAdapter;
import com.morning.morning.dungeon.Adapter.CheckAdapter;
import com.morning.morning.dungeon.DialogFragment.ShareDialogFragment;
import com.morning.morning.dungeon.Fragment.ProductImageFragment;
import com.morning.morning.dungeon.Fragment.ProductQaFragment;
import com.morning.morning.dungeon.Items.AmountItem;
import com.morning.morning.dungeon.Items.CheckItem;
import com.morning.morning.dungeon.Items.CouponItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;
import com.morning.morning.dungeon.View.NewCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/*
    상품정보 액티비티
 */

public class ProductActivity extends NewCompatActivity implements View.OnClickListener {

    private SliderLayout sliderLayout;
    private Button btn_ProductInfo, btn_InfoBuy, btn_Question, btn_Buy, btn_Recommend, btn_RealBuy;
    private FrameLayout frameLayout, frameSpinner;
    private ListView mListView;
    private ListView AmountListView;
    private CheckAdapter checkAdapter;
    private AmountAdapter amountAdapter;
    private CouponItem arg;

    private Boolean isClickBuy = false;
    private Boolean isAnniEnd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_36dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle("상품설명");

        init();
    }

    private void init() {

        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        btn_ProductInfo = (Button) findViewById(R.id.product_btn1);
        btn_InfoBuy = (Button) findViewById(R.id.product_btn2);
        btn_Question = (Button) findViewById(R.id.product_btn3);
        btn_Buy = (Button) findViewById(R.id.product_buy);
        btn_Recommend = (Button) findViewById(R.id.product_recommend);
        frameLayout = (FrameLayout) findViewById(R.id.fragment_product);
        frameSpinner = (FrameLayout) findViewById(R.id.product_spinner);
        btn_RealBuy = (Button) findViewById(R.id.product_realbuy);
        btn_ProductInfo.setOnClickListener(this);
        btn_InfoBuy.setOnClickListener(this);
        btn_Question.setOnClickListener(this);
        btn_Buy.setOnClickListener(this);
        btn_Recommend.setOnClickListener(this);
        btn_RealBuy.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.product_checklist);
        AmountListView = (ListView) findViewById(R.id.product_amountlist);

        checkAdapter = new CheckAdapter(this);
        amountAdapter = new AmountAdapter(this);

        AmountListView.setAdapter(amountAdapter);

        arg = getIntent().getExtras().getParcelable("arg");
        ((TextView) findViewById(R.id.product_title)).setText(arg.couponName);
        ((TextView) findViewById(R.id.product_smalltitle)).setText(arg.couponSubName);
        ((TextView) findViewById(R.id.product_price)).setText(arg.couponPrice + "원");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(((CheckBox)view.findViewById(R.id.listitem_checkcheck)).isChecked()) {
                    ((CheckBox)view.findViewById(R.id.listitem_checkcheck)).setChecked(false);
                    checkAdapter.m_List.get(position).isCheck = false;
                } else {
                    ((CheckBox)view.findViewById(R.id.listitem_checkcheck)).setChecked(true);
                    checkAdapter.m_List.get(position).isCheck = true;
                }

                Log.d("size", amountAdapter.m_List.size() + "");
                if(amountAdapter.m_List.size() == 0) {
                    if(checkAdapter.m_List.get(position).isCheck) {
                        Log.d("CHECK", "ADD");
                        AmountItem item = new AmountItem();
                        item.nameItem = checkAdapter.m_List.get(position).name;
                        item.saleReceiptNo = checkAdapter.m_List.get(position).saleReceiptNo;
                        amountAdapter.addItem(item);
                        amountAdapter.notifyDataSetChanged();
                    }
                }

                for(int i=0;i<amountAdapter.m_List.size();i++) {

                    if(!amountAdapter.m_List.get(i).nameItem.equals(checkAdapter.m_List.get(position).name)) {
                        if(checkAdapter.m_List.get(position).isCheck) {
                            Log.d("CHECK", "ADD");
                            AmountItem item = new AmountItem();
                            item.nameItem = checkAdapter.m_List.get(position).name;
                            item.saleReceiptNo = checkAdapter.m_List.get(position).saleReceiptNo;
                            amountAdapter.addItem(item);
                            amountAdapter.notifyDataSetChanged();
                        }

                    } else {
                        if(!checkAdapter.m_List.get(position).isCheck) {
                            Log.d("CHECK", "REMOVE");
                            amountAdapter.removeItem(i);
                            amountAdapter.notifyDataSetChanged();
                        }
                    }
                }

                if(amountAdapter.m_List.size() > 0) {
                    ((FrameLayout) findViewById(R.id.product_spinnerz)).setVisibility(View.VISIBLE);
                    ((FrameLayout) findViewById(R.id.product_buyframe)).setVisibility(View.VISIBLE);
                } else {
                    ((FrameLayout) findViewById(R.id.product_spinnerz)).setVisibility(View.GONE);
                    ((FrameLayout) findViewById(R.id.product_buyframe)).setVisibility(View.GONE);
                }
            }
        });

        try {
            JSONArray sliderArray = new JSONArray(arg.sliderObject);
            for(int i=0;i<sliderArray.length();i++) {
                JSONObject jsonObject = sliderArray.getJSONObject(i);
                addSlider(jsonObject.getString("fileUri"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        attachProductInfo();
    }

    private void addSlider(String link) {
        TextSliderView textSliderView = new TextSliderView(ProductActivity.this);
        textSliderView
                .image(link)
                .setScaleType(BaseSliderView.ScaleType.Fit);
        sliderLayout.addSlider(textSliderView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
        return true;
    }

    private void onClickButton(int id) {
        btn_ProductInfo.setBackgroundColor(Color.parseColor("#EEEEEE"));
        btn_InfoBuy.setBackgroundColor(Color.parseColor("#EEEEEE"));
        btn_Question.setBackgroundColor(Color.parseColor("#EEEEEE"));

        ((Button) findViewById(id)).setBackgroundColor(Color.parseColor("#626262"));
    }

    private void attachProductInfo() {
        Bundle bundle = new Bundle();
        Log.d("link", arg.contentObject);
        bundle.putString("arrayImage", arg.contentObject);
        ProductImageFragment productImageFragment = new ProductImageFragment();
        productImageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_product, productImageFragment).commit();
    }

    private void attachProductQa() {
        Bundle bundle = new Bundle();
        Log.d("info", arg.Qa);
        bundle.putString("info", arg.Qa);
        ProductQaFragment productQaFragment = new ProductQaFragment();
        productQaFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_product, productQaFragment).commit();
    }

    private void buyButtonHandler() {

        if(isClickBuy == false) {

            isClickBuy = true;
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    isAnniEnd = false;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAnniEnd = true;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Log.d("size", arg.List_couponName.size() + "");
            if (true) {
                for (int i = 0; i < arg.List_couponName.size(); i++) {
                    CheckItem item = new CheckItem();
                    item.name = arg.List_couponName.get(i);
                    item.saleReceiptNo = Integer.parseInt(arg.List_saleProductNum.get(i));
                    checkAdapter.addItem(item);
                }
                checkAdapter.notifyDataSetChanged();
            }

            frameSpinner.setVisibility(View.VISIBLE);
            frameSpinner.startAnimation(animation);
            mListView.setAdapter(checkAdapter);

            btn_Buy.setText("취소하기");

        } else {
            if(isAnniEnd == true) {
                btn_Buy.setText("구매하기");
                frameSpinner.setVisibility(View.GONE);
                ((FrameLayout) findViewById(R.id.product_spinnerz)).setVisibility(View.GONE);
                ((FrameLayout) findViewById(R.id.product_buyframe)).setVisibility(View.GONE);
                amountAdapter.resetItem();
                amountAdapter.notifyDataSetChanged();
                isClickBuy = false;
                checkAdapter.resetItem();
            }
        }
    }

    private void addCart(String UserNo) {

        ProgressDialog progressDialog = null;

        if(amountAdapter.m_List.size() == 0) {
            Toast.makeText(ProductActivity.this, "상품을 1개이상 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog = ProgressDialog.show(this, "", "로그인 중 입니다..", true);

            for(int i=0;i<amountAdapter.getCount();i++) {

                Log.e("ADDCART", ""+amountAdapter.m_List.get(i).saleReceiptNo + "     " + amountAdapter.m_List.get(i).amountItem);
                Util.getNetworkService().addCart("1001", String.valueOf(amountAdapter.m_List.get(i).saleReceiptNo), String.valueOf(amountAdapter.m_List.get(i).amountItem), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                            Log.d("content", jsonObject.toString());
                            if(Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {
                                Toast.makeText(ProductActivity.this, "장바구니 등록성공!", Toast.LENGTH_SHORT).show();
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
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.product_btn1 || id == R.id.product_btn2 || id == R.id.product_btn3) {
            onClickButton(id);
        }

        switch (id)
        {
            case R.id.product_btn1:
                attachProductInfo();
                break;
            case R.id.product_btn2:
                break;
            case R.id.product_btn3:
                attachProductQa();
                break;
            case R.id.product_buy:
                buyButtonHandler();
                break;
            case R.id.product_recommend:
                ShareDialogFragment shareDialogFragment = ShareDialogFragment.newInstance("DUNGEON", "CONTENT");
                shareDialogFragment.show(getSupportFragmentManager(), "SHARE");
                break;
            case R.id.product_realbuy:
                addCart("11");
                break;
        }
    }
}
