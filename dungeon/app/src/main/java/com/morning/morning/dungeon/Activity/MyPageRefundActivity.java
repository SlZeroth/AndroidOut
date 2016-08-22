package com.morning.morning.dungeon.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.morning.morning.dungeon.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyPageRefundActivity extends AppCompatActivity {

    @InjectView(R.id.refund_listview)
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_refund);
        ButterKnife.inject(this);
    }
}
