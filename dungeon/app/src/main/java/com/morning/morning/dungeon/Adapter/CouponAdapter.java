package com.morning.morning.dungeon.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.morning.morning.dungeon.Items.CouponItem;
import com.morning.morning.dungeon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 10. 17..
 */
public class CouponAdapter extends BaseAdapter {

    private ArrayList<CouponItem> m_List;
    private Context mContext;

    public CouponAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<CouponItem>();
    }

    public void addItem(CouponItem item) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_coupon, parent, false);
        }

        ImageView imageTitle = (ImageView) convertView.findViewById(R.id.coupon_image);
        TextView CouponName = (TextView) convertView.findViewById(R.id.coupon_name);
        TextView CouponPrice = (TextView) convertView.findViewById(R.id.coupon_price);
        TextView CouponSubName = (TextView) convertView.findViewById(R.id.coupon_subname);
        TextView CouponSalePrice = (TextView) convertView.findViewById(R.id.coupon_saleprice);
        TextView CouponQty = (TextView) convertView.findViewById(R.id.coupon_qty);
        //m_List.get(position).thumbnailLink
        Picasso.with(mContext).load(m_List.get(position).thumbnailLink).fit().into(imageTitle);
        CouponName.setText(m_List.get(position).couponName);
        CouponSalePrice.setText(m_List.get(position).couponSalePrice + "원  ");
        CouponQty.setText(m_List.get(position).qty + "% ");
        CouponSubName.setText(m_List.get(position).couponSubName);
        CouponPrice.setText(m_List.get(position).couponPrice + "원 ");
        return convertView;
    }
}
