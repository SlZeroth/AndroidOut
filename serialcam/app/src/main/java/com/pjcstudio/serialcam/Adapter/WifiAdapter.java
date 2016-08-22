package com.pjcstudio.serialcam.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pjcstudio.serialcam.R;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 12. 24..
 */
public class WifiAdapter extends BaseAdapter {

    private ArrayList<String> m_List;
    private Context mContext;

    public WifiAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<String>();
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_text, viewGroup, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.item_text);
        textView.setText(m_List.get(i));
        return convertView;
    }
}
