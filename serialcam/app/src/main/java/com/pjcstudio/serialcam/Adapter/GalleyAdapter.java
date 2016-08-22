package com.pjcstudio.serialcam.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pjcstudio.serialcam.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 10. 9..
 */
public class GalleyAdapter extends BaseAdapter {

    public ArrayList<String> m_List;
    private Context mContext;
    private int height_Grid, width_Grid;

    public GalleyAdapter(Context context, int height_top, int height_grid) {
        mContext = context;
        m_List = new ArrayList<String>();
        width_Grid = height_top;
        height_Grid = height_grid;
    }

    public void setArrayList(ArrayList<String> array) {
        m_List = array;
    }

    public void addItem(String str) {
        m_List.add(str);
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int i) {
        return m_List.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, viewGroup, false);
        }
        init(convertView, position);
        return convertView;
    }

    public void setMarker(ImageView imageView) {
    }

    private void init(View v, int pos) {
        ImageView imageView = (ImageView) v.findViewById(R.id.item_image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        Log.d("eh", ""+height_Grid/2);
        //layoutParams.height = height_Grid/2;
        imageView.setLayoutParams(layoutParams);

        if(m_List.get(pos).equals("NONE")) {
            imageView.setBackgroundColor(Color.parseColor("#00000000"));
        } else {
            Picasso.with(mContext).load(new File(m_List.get(pos))).fit().into(imageView);
        }
    }
}
