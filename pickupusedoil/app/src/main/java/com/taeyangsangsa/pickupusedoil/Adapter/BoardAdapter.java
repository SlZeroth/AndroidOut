package com.taeyangsangsa.pickupusedoil.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taeyangsangsa.pickupusedoil.Items.BoardItem;
import com.taeyangsangsa.pickupusedoil.R;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 8. 25..
 */
public class BoardAdapter extends BaseAdapter {

    private ArrayList<BoardItem> m_List;
    private Context mContext;

    public BoardAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<BoardItem>();
    }

    public void addItem(BoardItem item) {
        m_List.add(item);
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
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            if(m_List.get(position).viewtype == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_boardroot, viewGroup, false);
                viewHolder.date = (TextView) convertView.findViewById(R.id.board_root);
            } else {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_board, viewGroup, false);
                viewHolder.companyName = (TextView) convertView.findViewById(R.id.tv_bar_title);
                viewHolder.companyPhoneNumber = (TextView) convertView.findViewById(R.id.tv_bar_number);
                viewHolder.comment = (TextView) convertView.findViewById(R.id.tv_bar_text);
                viewHolder.day = (TextView) convertView.findViewById(R.id.imv_bar_date);
                viewHolder.companyAddress = (TextView) convertView.findViewById(R.id.tv_bar_address);
                viewHolder.getday = (TextView) convertView.findViewById(R.id.tv_bar_getday);
                viewHolder.owner = (TextView) convertView.findViewById(R.id.tv_bar_owner);
                viewHolder.sugo = (TextView) convertView.findViewById(R.id.tv_bar_sugo);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(m_List.get(position).viewtype == 0) {
            viewHolder.date.setText(
                    m_List.get(position).year + "년" +
                            m_List.get(position).month + "월"
            );
        } else {
            viewHolder.companyName.setText(m_List.get(position).companyName);
            viewHolder.companyPhoneNumber.setText(m_List.get(position).companyPhoneNumber);
            viewHolder.comment.setText(m_List.get(position).comment);
            viewHolder.companyAddress.setText(m_List.get(position).companyAddress);
            viewHolder.day.setText(m_List.get(position).day);
            viewHolder.getday.setText("수거요청일 : " + m_List.get(position).getday);
            viewHolder.owner.setText("수거책임자 : " + m_List.get(position).sugocompany);
            if(m_List.get(position).sugoflag.equals("1")) {
                viewHolder.sugo.setText("수거 대기중 ");
                viewHolder.sugo.setTextColor(Color.RED);
            } else {
                viewHolder.sugo.setText("처리 완료 ");
                viewHolder.sugo.setTextColor(Color.BLUE);
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return m_List.get(position).viewtype;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    static class ViewHolder {
        public TextView companyName;
        public TextView companyPhoneNumber;
        public TextView companyAddress;
        public TextView comment;
        public TextView day;
        public TextView date;
        public TextView getday;
        public TextView owner;
        public TextView sugo;
    }
}
