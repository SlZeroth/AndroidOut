package com.morning.morning.dungeon.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.morning.morning.dungeon.Items.AmountItem;
import com.morning.morning.dungeon.R;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 12. 12..
 * 상품명 수량적는 어댑터
 */
public class AmountAdapter extends BaseAdapter {

    public ArrayList<AmountItem> m_List;
    private Context mContext;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button btn = (Button) v;
            Integer integer = (Integer) v.getTag();
            int result = 1;

            if(btn.getText().toString().equals("+")) {
                result = m_List.get(integer).amountItem + 1;
            } else if(btn.getText().toString().equals("-")) {
                result = m_List.get(integer).amountItem - 1;
            }
            m_List.get(integer).amountItem = result;

            //info.text.setText(result+"");
            //Log.d("count", m_List.get(info.position).amountItem + "");
        }
    };

    public AmountAdapter(Context context) {
        mContext = context;
        m_List = new ArrayList<AmountItem>();

    }

    public void addItem(AmountItem item) {
        m_List.add(item);
    }

    public void removeItem(int pos) {
        m_List.remove(pos);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_amount, parent, false);
            viewHolder.btn_p = (Button) convertView.findViewById(R.id.amount_p);
            viewHolder.btn_m = (Button) convertView.findViewById(R.id.amount_m);
            viewHolder.text = (TextView) convertView.findViewById(R.id.amount_itemname);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //itemInfo info = new itemInfo();
        //info.text = (TextView) convertView.findViewById(R.id.amount_result);
        //info.position = position;

        Integer integer = position;

        viewHolder.btn_p.setTag(integer);
        viewHolder.btn_m.setTag(integer);
        viewHolder.btn_p.setOnClickListener(onClickListener);
        viewHolder.btn_m.setOnClickListener(onClickListener);
        viewHolder.text.setText(m_List.get(position).nameItem);

        return convertView;
    }

    private class ViewHolder {
        public Button btn_p;
        public Button btn_m;
        public TextView text;
    }

    private class itemInfo {
        public TextView text;
        public int productNum;
        public int position;
    }

}
