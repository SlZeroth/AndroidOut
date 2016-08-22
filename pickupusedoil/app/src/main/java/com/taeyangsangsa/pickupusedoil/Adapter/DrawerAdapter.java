package com.taeyangsangsa.pickupusedoil.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taeyangsangsa.pickupusedoil.R;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 8. 25..
 */
public class DrawerAdapter extends BaseAdapter {

    private ArrayList<String> m_List;
    private Context mContext;

    public DrawerAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<String>();
    }

    public int addItem(String item) {
        m_List.add(item);
        return 1;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.draweritem_item, viewGroup, false);
        }
        TextView textview = (TextView) convertView.findViewById(R.id.drawer_text);
        textview.setText(m_List.get(position));
        return convertView;
    }
}
