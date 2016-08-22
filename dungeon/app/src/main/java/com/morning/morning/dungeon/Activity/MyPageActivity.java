package com.morning.morning.dungeon.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.morning.morning.dungeon.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MyPageActivity extends AppCompatActivity {

    @InjectView(R.id.mypage_return)
    LinearLayout btn_Return;
    @InjectView(R.id.mypage_basket)
    LinearLayout btn_Basket;
    @InjectView(R.id.mypage_mybuylist)
    LinearLayout btn_BuyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        ButterKnife.inject(this);

    }

    @OnClick(R.id.mypage_return)
    void onReturnClick() {
        Intent intent = new Intent(this, MyPageRefundActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mypage_basket)
    void onBasketClick() {
        Intent intent = new Intent(this, BasketActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.mypage_mybuylist)
    void onMyBuyList() {
        Intent intent = new Intent(this, OrderListActivity.class);
        startActivity(intent);
    }
}
