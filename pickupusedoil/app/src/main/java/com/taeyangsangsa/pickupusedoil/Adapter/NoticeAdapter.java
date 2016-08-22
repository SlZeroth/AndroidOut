package com.taeyangsangsa.pickupusedoil.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taeyangsangsa.pickupusedoil.Items.NoticeChildItem;
import com.taeyangsangsa.pickupusedoil.Items.NoticeGroupItem;
import com.taeyangsangsa.pickupusedoil.R;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 8. 27..
 */
public class NoticeAdapter extends BaseExpandableListAdapter {

    private ArrayList<NoticeGroupItem> m_GroupList;
    private ArrayList<ArrayList<NoticeChildItem>> m_ChildList;
    private Context mContext;

    public NoticeAdapter(Context context, ArrayList<NoticeGroupItem> group, ArrayList<ArrayList<NoticeChildItem>> child) {
        mContext = context;
        m_GroupList = group;
        m_ChildList = child;
    }

    public void setItem(ArrayList group, ArrayList child) {
        m_GroupList = group;
        m_ChildList = child;
    }

    @Override
    public int getGroupCount() {
        return m_GroupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return m_ChildList.get(i).size();
    }

    @Override
    public Object getGroup(int i) {
        return m_GroupList.get(i);
    }

    @Override
    public Object getChild(int i, int i2) {
        return m_ChildList.get(i).get(i2);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_notice_group, viewGroup, false);
        }

        ((TextView)convertView.findViewById(R.id.noticgroup_title)).setText(m_GroupList.get(i).title);
        ((TextView)convertView.findViewById(R.id.noticgroup_date)).setText(m_GroupList.get(i).date);

        if(isExpanded) {
            Log.d("state", "펼침");
            ((ImageView)convertView.findViewById(R.id.noticgroup_image)).setImageResource(R.drawable.ic_keyboard_arrow_down_white_24dp);
        } else {
            Log.d("state", "닫음");
            ((ImageView)convertView.findViewById(R.id.noticgroup_image)).setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupid, int childid, boolean b, View convertView, ViewGroup viewGroup) {
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listitem_notice_child, viewGroup, false);
        }

        NoticeChildItem noticeChildItem = (NoticeChildItem) getChild(groupid,childid);
        ((TextView)convertView.findViewById(R.id.noticchild_content)).setText(noticeChildItem.content);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }
}
