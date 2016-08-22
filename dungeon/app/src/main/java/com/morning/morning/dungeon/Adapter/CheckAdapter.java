package com.morning.morning.dungeon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.morning.morning.dungeon.Items.CheckItem;
import com.morning.morning.dungeon.R;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 12. 6..
 */
public class CheckAdapter extends BaseAdapter {

    public ArrayList<CheckItem> m_List;
    private Context mContext;

    public CheckAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<CheckItem>();
    }

    public void addItem(CheckItem item) {
        m_List.add(item);
    }

    public void resetItem() {
        m_List.clear();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_check, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.listitem_checkname)).setText(m_List.get(position).name);

        return convertView;
    }
}
