package com.morning.morning.dungeon.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by juchan on 16. 1. 10..
 */
public class RefundAdapter extends BaseAdapter {

    private ArrayList<String> m_List;
    private Context mContext;

    public RefundAdapter(Context context) {
        m_List = new ArrayList<String>();
        mContext = context;
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
        return null;
    }
}
