package com.blackpjcstudio.freewating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blackpjcstudio.freewating.Items.BoardItem;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 11. 27..
 */
public class BoardAdapter extends BaseAdapter {

    private ArrayList<BoardItem> m_List;
    private Context mContext;

    public BoardAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<BoardItem>();
    }

    public void clearItem() {
        m_List.clear();
    }

    public void addItem(BoardItem item) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_board, parent, false);
        }

        TextView textTitle = (TextView) convertView.findViewById(R.id.item_text);
        textTitle.setText(m_List.get(position).castName);

        return convertView;
    }
}
