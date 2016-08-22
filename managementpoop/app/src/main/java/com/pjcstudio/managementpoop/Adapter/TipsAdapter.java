package com.pjcstudio.managementpoop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjcstudio.managementpoop.Items.TipsItem;
import com.pjcstudio.managementpoop.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by juchanpark on 2015. 8. 5..
 */
public class TipsAdapter extends BaseAdapter {

    private ArrayList<TipsItem> m_List;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public TipsAdapter(Context context) {
        super();
        mContext = context;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int setArrayItem(ArrayList<TipsItem> arraylist) {
        m_List = arraylist;
        return 1;
    }

    public int clearItem() {
        m_List.clear();
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
    public View getView(int pos, View view, ViewGroup viewGroup) {
        View v = view;
            v = layoutInflater.inflate(R.layout.listitem_boarditem, viewGroup, false);
            Inititem(v, pos);
        return v;
    }

    private void Inititem(View v, int pos) {
        ImageView imageView = (ImageView) v.findViewById(R.id.boarditem_photo);
        TextView textView = (TextView) v.findViewById(R.id.boarditem_title);
        textView.setText(m_List.get(pos).title);
        Picasso.with(mContext).load(m_List.get(pos).bglink).fit().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Log.i("good", "good");
            }

            @Override
            public void onError() {
            }
        });
    }
}