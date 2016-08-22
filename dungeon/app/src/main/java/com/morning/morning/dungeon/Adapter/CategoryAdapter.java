package com.morning.morning.dungeon.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.morning.morning.dungeon.Items.CategoryItem;
import com.morning.morning.dungeon.R;

import java.util.ArrayList;

/**
 * Created by juchan on 16. 2. 13..
 */
public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CategoryItem> m_List;

    public CategoryAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<CategoryItem>();
    }

    public void addItem(CategoryItem item) {
        m_List.add(item);
    }

    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_category, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.listitem_category);
        Log.d("dd", "dd");
        textView.setText(m_List.get(position).Name);

        return convertView;
    }
}
