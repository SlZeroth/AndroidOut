package com.pjcstudio.managementpoop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pjcstudio.managementpoop.R;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 8. 14..
 */
public class MapAdapter extends BaseAdapter {

    private ArrayList<String> m_List;
    private Context mContext;

    public MapAdapter(Context context) {
        m_List = new ArrayList<String>();
        mContext = context;
    }

    public void setListItem(ArrayList<String> list) {
        m_List = list;
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
    public View getView(int pos, View view, ViewGroup viewGroup) {
        View rootView = null;
        if(view == null) {
            rootView = (View) LayoutInflater.from(mContext).inflate(R.layout.listitem_textview, viewGroup, false);
            TextView textView = (TextView) rootView.findViewById(R.id.listitem_textview);
            textView.setText(m_List.get(pos));
        } else {
            rootView = view;
            TextView textView = (TextView) rootView.findViewById(R.id.listitem_textview);
            textView.setText(m_List.get(pos));
        }
        return rootView;
    }
}
