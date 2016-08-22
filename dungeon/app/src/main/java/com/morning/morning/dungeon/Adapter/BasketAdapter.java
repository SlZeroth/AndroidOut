package com.morning.morning.dungeon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.morning.morning.dungeon.Items.BasketItem;
import com.morning.morning.dungeon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 12. 5..
 */
public class BasketAdapter extends BaseAdapter {

    public ArrayList<BasketItem> m_List;
    private Context mContext;

    public BasketAdapter(Context context) {
        m_List = new ArrayList<BasketItem>();
        mContext = context;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {



        }
    };

    public void addItem(BasketItem item) {
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

        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_basket, parent, false);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.basket_checkbox);
            viewHolder.textPrice = (TextView) convertView.findViewById(R.id.basket_price);
            viewHolder.textSelect = (TextView) convertView.findViewById(R.id.basket_textselect);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.basket_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(m_List.get(position).imageLink).fit().into(viewHolder.imageView);
        viewHolder.textPrice.setText(m_List.get(position).textPrice + " 원");
        viewHolder.textSelect.setText(m_List.get(position).textSelect);
        viewHolder.checkBox.setText(m_List.get(position).checkboxStr);

        return convertView;
    }

    public class ViewHolder {
        public ImageView imageView;
        public CheckBox checkBox;
        public TextView textPrice;
        public TextView textSelect;
    }
}
