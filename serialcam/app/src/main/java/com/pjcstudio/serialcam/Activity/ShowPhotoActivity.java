package com.pjcstudio.serialcam.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.pjcstudio.serialcam.R;
import com.squareup.picasso.Picasso;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowPhotoActivity extends AppCompatActivity {

    ImageView imageView;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent myIntent = getIntent();
        imageView = (ImageView) findViewById(R.id.show_image);
        Picasso.with(this).load(new File(myIntent.getStringExtra("path"))).fit().into(imageView);
        mAttacher = new PhotoViewAttacher(imageView);

        Toast.makeText(ShowPhotoActivity.this, new File(myIntent.getStringExtra("path")).length() + "", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
