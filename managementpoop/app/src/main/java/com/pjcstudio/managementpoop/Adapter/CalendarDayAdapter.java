package com.pjcstudio.managementpoop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pjcstudio.managementpoop.Items.CalendarDayItem;
import com.pjcstudio.managementpoop.Items.CalendarDaySubItem;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.Iteminfo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by juchanpark on 2015. 7. 27..
 */
public class CalendarDayAdapter extends BaseAdapter {

    private ArrayList<CalendarDayItem> m_List;
    private Context mContext;
    private LayoutInflater inflater;

    public CalendarDayAdapter(Context context) {
        super();
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_List = new ArrayList<CalendarDayItem>();

    }

    public void setListItem(ArrayList<CalendarDayItem> Lists) {
        m_List = Lists;
    }

    public int addItem(CalendarDayItem item) {
        m_List.add(item);
        return 1;
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
        convertView = inflater.inflate(R.layout.listitem_calendar_day, viewGroup, false);
        Log.d("ITEM", i + " : " + m_List.get(i).List_poop.toString());
        setLayout(convertView, i);
        return convertView;
    }

    public void setLayout(View v, int itemNum) {
        CalendarDayItem listItem = m_List.get(itemNum);
        ((TextView)v.findViewById(R.id.day)).setText(String.valueOf(itemNum+1)); // 아이템 날짜 setText
        TextView textDayDay = (TextView) v.findViewById(R.id.day_day);
        LinearLayout topLayout = (LinearLayout) v.findViewById(R.id.lintop);
        LinearLayout bottomLayout = (LinearLayout) v.findViewById(R.id.linbottom);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Log.d("item", listItem.year);
        Log.d("item2", listItem.month);
        Log.d("item3", listItem.day);
        calendar.set(Calendar.YEAR, Integer.parseInt(listItem.year));
        calendar.set(Calendar.MONTH, Integer.parseInt(listItem.month)-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(listItem.day));

        switch(calendar.get(Calendar.DAY_OF_WEEK))
        {
            case 1:
                textDayDay.setText("SUN");
                break;
            case 2:
                textDayDay.setText("MON");
                break;
            case 3:
                textDayDay.setText("TUE");
                break;
            case 4:
                textDayDay.setText("WED");
                break;
            case 5:
                textDayDay.setText("THU");
                break;
            case 6:
                textDayDay.setText("FRI");
                break;
            case 7:
                textDayDay.setText("SAT");
                break;
        }

        Log.d("CalendarDay", "year : " + listItem.year + " month : " + listItem.month);
        if(Integer.parseInt(listItem.year) == year && Integer.parseInt(listItem.month) == month && Integer.parseInt(listItem.day) == day) {
            ImageView relativeLayouts = (ImageView) v.findViewById(R.id.calendarday_image);
            relativeLayouts.setImageResource(R.drawable.today);
            relativeLayouts.setBackgroundColor(Color.parseColor("#fff3f3f3"));
        }

        for(int i=0;i<listItem.List_poop.size();i++) {
            Log.d("position", String.valueOf(itemNum));
            CalendarDaySubItem subItem = listItem.List_poop.get(i);
            subItem.hour = String.format("%02d", Integer.parseInt(subItem.hour));
            String time = null;
            if(Integer.parseInt(subItem.hour) >= 12) {
                time = subItem.hour + ":" + subItem.minute + " pm";
            } else {
                time = subItem.hour + ":" + subItem.minute + " am";
            }
            TextView textTime = getTextView(time, 0, 1);
            bottomLayout.addView(textTime);
            ImageView imageView = getImageView(Integer.parseInt(subItem.type), 0);
            bottomLayout.addView(imageView);
            TextView textItemName = getTextView(subItem.type, 1, 0);
            bottomLayout.addView(textItemName);
            View view = makeBar();
            bottomLayout.addView(view);
        }
        for(int i=0;i<listItem.List_food.size();i++) {
            Log.d("position", String.valueOf(itemNum));
            CalendarDaySubItem subItem = listItem.List_food.get(i);
            //String time = subItem.hour + ":" + subItem.minute + " am";
            String time = null;
            if(Integer.parseInt(subItem.hour) >= 12) {
                time = subItem.hour + ":" + subItem.minute + " pm";
            } else {
                time = subItem.hour + ":" + subItem.minute + " am";
            }
            TextView textTime = getTextView(time, 0, 1);
            topLayout.addView(textTime);
            ImageView imageView = getImageView(Integer.parseInt(subItem.type), 1);
            topLayout.addView(imageView);
            TextView textItemName = getTextView(subItem.type, 2, 0);
            topLayout.addView(textItemName);
            View view = makeBar();
            topLayout.addView(view);
        }
    }

    private View makeBar() {
        View view = new View(mContext);
        final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, mContext.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, mContext.getResources().getDisplayMetrics()),
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, mContext.getResources().getDisplayMetrics()),
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, mContext.getResources().getDisplayMetrics()),
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, mContext.getResources().getDisplayMetrics())
        );
        view.setLayoutParams(params);
        view.setBackgroundColor(Color.parseColor("#d3d3d3"));
        return view;
    }

    private ImageView getImageView(int type, int itemtype) {
        int itemId = 0;
        if(itemtype == 0) {
            switch (type) {
                case 1:
                    itemId = R.drawable.poop_1;
                    break;
                case 2:
                    itemId = R.drawable.poop_2;
                    break;
                case 3:
                    itemId = R.drawable.poop_3;
                    break;
                case 4:
                    itemId = R.drawable.poop_4;
                    break;
                case 5:
                    itemId = R.drawable.poop_5;
                    break;
                case 6:
                    itemId = R.drawable.poop_6;
                    break;
            }
        }
        else if(itemtype == 1) {
            switch(type)
            {
                case 1:
                    itemId = R.drawable.food_1;
                    break;
                case 2:
                    itemId = R.drawable.food_2;
                    break;
                case 3:
                    itemId = R.drawable.food_3;
                    break;
                case 4:
                    itemId = R.drawable.food_4;
                    break;
                case 5:
                    itemId = R.drawable.food_5;
                    break;
                case 6:
                    itemId = R.drawable.food_6;
                    break;
            }
        }
        ImageView imageView = new ImageView(mContext);
        final int width = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, mContext.getResources().getDisplayMetrics());
        final int height = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, mContext.getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.gravity = Gravity.CENTER;
        params.setMargins(
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, mContext.getResources().getDisplayMetrics()),
                0,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, mContext.getResources().getDisplayMetrics()),
                0
        );
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageResource(itemId);
        return imageView;
    }

    private TextView getTextView(String text, int style, int textstyle) {

        String itemtext = null;
        int type = 0;

        if(style == 1 || style == 2) {
            type = Integer.parseInt(text);
        }
        if(style == 1) {
            switch (type) {
                case 1:
                    itemtext = Iteminfo.POOP_ITEM1;
                    break;
                case 2:
                    itemtext = Iteminfo.POOP_ITEM2;
                    break;
                case 3:
                    itemtext = Iteminfo.POOP_ITEM3;
                    break;
                case 4:
                    itemtext = Iteminfo.POOP_ITEM4;
                    break;
                case 5:
                    itemtext = Iteminfo.POOP_ITEM5;
                    break;
                case 6:
                    itemtext = Iteminfo.POOP_ITEM6;
                    break;
            }
        }
        else if(style == 2) {
            switch (type)
            {
                case 1:
                    itemtext = Iteminfo.FOOD_ITEM1;
                    break;
                case 2:
                    itemtext = Iteminfo.FOOD_ITEM2;
                    break;
                case 3:
                    itemtext = Iteminfo.FOOD_ITEM3;
                    break;
                case 4:
                    itemtext = Iteminfo.FOOD_ITEM4;
                    break;
                case 5:
                    itemtext = Iteminfo.FOOD_ITEM5;
                    break;
                case 6:
                    itemtext = Iteminfo.FOOD_ITEM6;
                    break;
            }
        }

        if(style == 0) {
            itemtext = text;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(params);
        textView.setText(itemtext);
        if(textstyle == 1) {
            textView.setTypeface(null, Typeface.BOLD);
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);

        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    public class ViewHolder {
    }
}
