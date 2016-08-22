package com.morning.morning.dungeon.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.morning.morning.dungeon.Items.GameItem;
import com.morning.morning.dungeon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by juchan on 16. 2. 18..
 */
public class GameAdapter extends BaseAdapter {

    public Context mContext;
    public ArrayList<GameItem> m_List;

    public GameAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<GameItem>();
    }

    public void addItem(GameItem item) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_game, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.game_name)).setText(m_List.get(position).gameName);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.game_image);
        Picasso.with(mContext).load(m_List.get(position).gameLink).fit().into(imageView);

        return convertView;
    }
}
