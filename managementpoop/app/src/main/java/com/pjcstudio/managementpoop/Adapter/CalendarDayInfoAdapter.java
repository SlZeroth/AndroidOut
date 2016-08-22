package com.pjcstudio.managementpoop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjcstudio.managementpoop.Items.CalendarDayInfoItem;
import com.pjcstudio.managementpoop.Items.CalendarDayItem;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.Iteminfo;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 7. 29..
 */
public class CalendarDayInfoAdapter extends BaseAdapter {

    private ArrayList<CalendarDayInfoItem> m_List;
    private Context mContext;
    private LayoutInflater inflater;

    public CalendarDayInfoAdapter(Context context) {
        super();
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_List = new ArrayList<CalendarDayInfoItem>();
    }

    public void setItemlist(ArrayList<CalendarDayInfoItem> arrayList) {
        m_List = arrayList;
    }

    public int dropItem(int pos) {
        m_List.remove(pos);
        return m_List.size();
    }

    public void clearItem() {
        m_List.clear();
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
        ViewHolder viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listitem_rootdayinfo, viewGroup, false);
            viewHolder.textTime = (TextView) convertView.findViewById(R.id.dayinfo_time);
            viewHolder.textMemo = (TextView) convertView.findViewById(R.id.dayinfo_memo);
            viewHolder.dot = (View) convertView.findViewById(R.id.dayinfo_dot);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.dayinfo_image);
            convertView.setTag(viewHolder);
        viewHolder = (ViewHolder) convertView.getTag();
        setLayout(viewHolder, i);
        return convertView;
    }

    private void setLayout(ViewHolder v, int pos) {
        int itemId = 0;
        StringBuffer sb = new StringBuffer();
        CalendarDayInfoItem item = m_List.get(pos);

        Log.d("CALENDAR", "INFo item");
        if(m_List.size() != 1 && pos != 0) {
            if(item.time == m_List.get(pos-1).time) {
                Log.d("POS", "pos : " + pos);
                v.textTime.setVisibility(View.INVISIBLE);
                v.dot.setVisibility(View.INVISIBLE);
            }
        }
        if(Integer.parseInt(item.hour) >= 12) {
            v.textTime.setText(item.hour + ":" + item.minute + " pm");
        } else {
            v.textTime.setText(item.hour + ":" + item.minute + " am");
        }

        if(item.itemtype.equals("P")) {
            int type = Integer.parseInt(item.type);
            switch (type)
            {
                case 1:
                    sb.append(Iteminfo.POOP_ITEM1);
                    itemId = R.drawable.poop_1;
                    break;
                case 2:
                    sb.append(Iteminfo.POOP_ITEM2);
                    itemId = R.drawable.poop_2;
                    break;
                case 3:
                    sb.append(Iteminfo.POOP_ITEM3);
                    itemId = R.drawable.poop_3;
                    break;
                case 4:
                    sb.append(Iteminfo.POOP_ITEM4);
                    itemId = R.drawable.poop_4;
                    break;
                case 5:
                    sb.append(Iteminfo.POOP_ITEM5);
                    itemId = R.drawable.poop_5;
                    break;
                case 6:
                    sb.append(Iteminfo.POOP_ITEM6);
                    itemId = R.drawable.poop_6;
                    break;
            }
        }
        else if(item.itemtype.equals("F")) {
            int type = Integer.parseInt(item.type);
            switch (type)
            {
                case 1:
                    sb.append(Iteminfo.FOOD_ITEM1);
                    itemId = R.drawable.food_1;
                    break;
                case 2:
                    sb.append(Iteminfo.FOOD_ITEM2);
                    itemId = R.drawable.food_2;
                    break;
                case 3:
                    sb.append(Iteminfo.FOOD_ITEM3);
                    itemId = R.drawable.food_3;
                    break;
                case 4:
                    sb.append(Iteminfo.FOOD_ITEM4);
                    itemId = R.drawable.food_4;
                    break;
                case 5:
                    sb.append(Iteminfo.FOOD_ITEM5);
                    itemId = R.drawable.food_5;
                    break;
                case 6:
                    sb.append(Iteminfo.FOOD_ITEM6);
                    itemId = R.drawable.food_6;
                    break;
            }
            if(!item.memo.equals(" ")) {
                sb.append(" - " + item.memo);
            }
        }
        v.textMemo.setText(sb.toString());
        v.imageView.setBackground(mContext.getResources().getDrawable(itemId));
    }

    static class ViewHolder {
        public TextView textTime;
        public TextView textMemo;
        public View dot;
        public ImageView imageView;
    }
}
