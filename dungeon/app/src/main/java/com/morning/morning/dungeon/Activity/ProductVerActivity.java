package com.morning.morning.dungeon.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.morning.morning.dungeon.Adapter.AmountAdapter;
import com.morning.morning.dungeon.Adapter.CheckAdapter;
import com.morning.morning.dungeon.Fragment.ProductImageFragment;
import com.morning.morning.dungeon.Fragment.ProductQaFragment;
import com.morning.morning.dungeon.Items.AmountItem;
import com.morning.morning.dungeon.Items.CheckItem;
import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.String.StateCode;
import com.morning.morning.dungeon.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class ProductVerActivity extends AppCompatActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.product_checklist)
    ListView mCheckListView;

    @InjectView(R.id.product_amountlist)
    ListView mAmountListView;

    @InjectView(R.id.product_btn1)
    Button btn_ProductInfo;

    @InjectView(R.id.product_btn2)
    Button btn_InfoBuy;

    @InjectView(R.id.product_btn3)
    Button btn_Question;

    @InjectView(R.id.product_buy)
    Button btn_buy;

    @InjectView(R.id.product_realbuy)
    Button btn_realBuy;

    @InjectView(R.id.viewpager)
    FrameLayout mFrameLayout;

    @InjectView(R.id.product_spinner)
    FrameLayout mFrameSpinner;

    @InjectView(R.id.slider)
    SliderLayout mSliderLayout;

    CheckAdapter mCheckAdapter;
    AmountAdapter mAmountAdapter;

    String Code;
    Boolean isClickBuy = false;
    Boolean isAnniEnd = true;

    // 아이템 이름
    String CouponName;
    // 아이템 서브이름
    String CouponSubName;
    // 가격
    String Price;
    // QA
    String howTo;
    // 본문 이미지오브젝트
    String ContentImage;
    // 슬라이드 이미지
    String SliderImage;

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(((CheckBox)view.findViewById(R.id.listitem_checkcheck)).isChecked()) {
                ((CheckBox)view.findViewById(R.id.listitem_checkcheck)).setChecked(false);
                mCheckAdapter.m_List.get(position).isCheck = false;
            } else {
                ((CheckBox)view.findViewById(R.id.listitem_checkcheck)).setChecked(true);
                mCheckAdapter.m_List.get(position).isCheck = true;
            }

            if(mAmountAdapter.m_List.size() == 0) {
                if(mCheckAdapter.m_List.get(position).isCheck) {
                    Log.d("CHECK", "ADD");
                    AmountItem item = new AmountItem();
                    item.nameItem = mCheckAdapter.m_List.get(position).name;
                    item.saleReceiptNo = mCheckAdapter.m_List.get(position).saleReceiptNo;
                    mAmountAdapter.addItem(item);
                    mAmountAdapter.notifyDataSetChanged();
                }
            }

            for(int i=0;i<mAmountAdapter.m_List.size();i++) {

                if(!mAmountAdapter.m_List.get(i).nameItem.equals(mCheckAdapter.m_List.get(position).name)) {
                    if(mCheckAdapter.m_List.get(position).isCheck) {
                        Log.d("CHECK", "ADD");
                        AmountItem item = new AmountItem();
                        item.nameItem = mCheckAdapter.m_List.get(position).name;
                        item.saleReceiptNo = mCheckAdapter.m_List.get(position).saleReceiptNo;
                        mAmountAdapter.addItem(item);
                        mAmountAdapter.notifyDataSetChanged();
                    }

                } else {
                    if(!mCheckAdapter.m_List.get(position).isCheck) {
                        Log.d("CHECK", "REMOVE");
                        mAmountAdapter.removeItem(i);
                        mAmountAdapter.notifyDataSetChanged();
                    }
                }
            }

            if(mAmountAdapter.m_List.size() > 0) {
                ((FrameLayout) findViewById(R.id.product_spinnerz)).setVisibility(View.VISIBLE);
                ((FrameLayout) findViewById(R.id.product_buyframe)).setVisibility(View.VISIBLE);
            } else {
                ((FrameLayout) findViewById(R.id.product_spinnerz)).setVisibility(View.GONE);
                ((FrameLayout) findViewById(R.id.product_buyframe)).setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_ver);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);

        Code = getIntent().getStringExtra("DisplayNo");

        // Toolbar 설정
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_36dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mCheckAdapter = new CheckAdapter(this);
        mAmountAdapter = new AmountAdapter(this);
        mCheckListView.setOnItemClickListener(onItemClickListener);
        mAmountListView.setAdapter(mAmountAdapter);


        getProduct();
    }

    public void getProduct() {

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "잠시만 기다려주세요.", true);

        Util.getNetworkService().getSaleReceiptInfosBySaleDisplayNo(Code, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                    if (Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {

                        JSONArray jsonArray = new JSONArray(jsonObject.getString("responseData"));

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject productInfoObject = jsonArray.getJSONObject(i).getJSONObject("productInfo");
                            Log.d("dd", productInfoObject.toString());
                            JSONArray informationsFileJSONArray = new JSONArray(productInfoObject.getString("informationsFileJSONArray"));
                            JSONArray headersFileJSONArray = new JSONArray(productInfoObject.getString("headersFileJSONArray"));

                            if (i == 0) {
                                ContentImage = informationsFileJSONArray.toString();
                                SliderImage = headersFileJSONArray.toString();
                                CouponName = productInfoObject.getString("productName");
                                CouponSubName = productInfoObject.getString("subjectText");
                                Price = jsonArray.getJSONObject(i).getString("price");
                                howTo = productInfoObject.getString("howbuyText");
                            }

                            CheckItem item = new CheckItem();
                            item.name = productInfoObject.getString("productName");
                            item.saleReceiptNo = Integer.parseInt(jsonArray.getJSONObject(i).getString("saleReceiptNo"));
                            mCheckAdapter.addItem(item);
                        }

                        progressDialog.dismiss();
                        addSlider(SliderImage);
                        initTextFields();
                        setTab1();
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

    private void addCart(String UserNo) {

        ProgressDialog progressDialog = null;

        if(mAmountAdapter.m_List.size() == 0) {
            Toast.makeText(ProductVerActivity.this, "상품을 1개이상 선택해주세요.", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog = ProgressDialog.show(this, "", "로그인 중 입니다..", true);

            for(int i=0;i<mAmountAdapter.getCount();i++) {

                Util.getNetworkService().addCart("1001", String.valueOf(mAmountAdapter.m_List.get(i).saleReceiptNo), String.valueOf(mAmountAdapter.m_List.get(i).amountItem), new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response2.getBody()).getBytes()));
                            Log.d("content", jsonObject.toString());
                            if(Integer.parseInt(jsonObject.getString("responseCode")) >= StateCode.STATE_SUCCESS) {
                                Toast.makeText(ProductVerActivity.this, "장바구니 등록성공!", Toast.LENGTH_SHORT).show();
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

    private void addSlider(String link) {
        TextSliderView textSliderView = new TextSliderView(ProductVerActivity.this);
        textSliderView
                .image(link)
                .setScaleType(BaseSliderView.ScaleType.Fit);
        mSliderLayout.addSlider(textSliderView);
    }

    private void initTextFields() {
        ((TextView) findViewById(R.id.product_title)).setText(CouponName);
        ((TextView) findViewById(R.id.product_smalltitle)).setText(CouponSubName);
        ((TextView) findViewById(R.id.product_price)).setText(Price + "원");
    }

    private void onClickButton(int id) {
        btn_ProductInfo.setBackgroundColor(Color.parseColor("#EEEEEE"));
        btn_InfoBuy.setBackgroundColor(Color.parseColor("#EEEEEE"));
        btn_Question.setBackgroundColor(Color.parseColor("#EEEEEE"));

        ((Button) findViewById(id)).setBackgroundColor(Color.parseColor("#626262"));
    }

    @OnClick(R.id.product_buy)
    void onBuy() {

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

            mFrameSpinner.setVisibility(View.VISIBLE);
            mFrameSpinner.startAnimation(animation);
            mCheckListView.setAdapter(mCheckAdapter);

            btn_buy.setText("취소하기");
        }
        else {
            if(isAnniEnd == true) {
                btn_buy.setText("구매하기");
                mFrameSpinner.setVisibility(View.GONE);
                ((FrameLayout) findViewById(R.id.product_spinnerz)).setVisibility(View.GONE);
                ((FrameLayout) findViewById(R.id.product_buyframe)).setVisibility(View.GONE);
                mAmountAdapter.resetItem();
                mAmountAdapter.notifyDataSetChanged();
                isClickBuy = false;
                mAmountAdapter.resetItem();
            }
        }

    }


    // 상품정보
    @OnClick(R.id.product_btn1)
    void setTab1() {
        onClickButton(R.id.product_btn1);
        Bundle bundle = new Bundle();
        bundle.putString("arrayImage", ContentImage);
        ProductImageFragment productImageFragment = new ProductImageFragment();
        productImageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, productImageFragment).commit();
    }

    @OnClick(R.id.product_btn3)
    void setTab3() {
        Bundle bundle = new Bundle();
        Log.d("info", howTo);
        bundle.putString("info", howTo);
        ProductQaFragment productQaFragment = new ProductQaFragment();
        productQaFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.viewpager, productQaFragment).commit();
    }

    @OnClick(R.id.product_realbuy)
    void onRealBuy() {
        addCart("1001");
    }
}
