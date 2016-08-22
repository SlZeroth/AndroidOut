package com.morning.morning.dungeon.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.morning.morning.dungeon.R;
import com.morning.morning.dungeon.View.NewCompatActivity;

import org.json.JSONArray;

public class PurchaseReconfirmActivity extends NewCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_reconfirm);
    }

    // 상품구매 네트워크통신
    private void ProductPurchase(JSONArray jsonArr) {

    }
}
