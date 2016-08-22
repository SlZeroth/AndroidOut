package com.morning.morning.dungeon.Activity;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.morning.morning.dungeon.DialogFragment.ShareDialogFragment;
import com.morning.morning.dungeon.R;

import java.util.List;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        ShareDialogFragment dialog = ShareDialogFragment.newInstance("ddd", "dddd");
        dialog.show(getSupportFragmentManager(), "SHARE");
    }
}
