package com.pjcstudio.managementpoop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.pjcstudio.managementpoop.R;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 8. 31..
 */
public class SlideAdapter extends BaseAdapter {

    private ArrayList<Bitmap> m_List;
    private Context mContext;

    public SlideAdapter(Context context) {
        m_List = new ArrayList<Bitmap>();
        mContext = context;
    }

    public void addItem(Bitmap bitmap) {
        m_List.add(bitmap);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_slide, viewGroup, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.slide_image);
        imageView.setBackground(new BitmapDrawable(mContext.getResources(), m_List.get(i)));
        return convertView;
    }
}
