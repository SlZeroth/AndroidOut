package com.morning.morning.dungeon.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by juchan on 2015. 11. 7..
 */
public class ProductImageFragment extends Fragment {

    private LinearLayout rootView;
    private Bundle arg;

    static final String ARRAY_IMAGE = "arrayImage";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = new LinearLayout(getActivity());
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        init(rootView);

        return rootView;
    }

    private void init(View v) {
        arg = getArguments();
        try {
            JSONArray imageArray = new JSONArray(arg.getString(ARRAY_IMAGE));
            for(int i=0;i<imageArray.length();i++) {
                JSONObject jsonObject = imageArray.getJSONObject(i);
                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Picasso.with(getActivity()).load(jsonObject.getString("fileUri")).into(imageView);
                rootView.addView(imageView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private class ProductImageAdapter extends BaseAdapter {

        private ArrayList<String> m_List;
        private Context mContext;

        public ProductImageAdapter(ArrayList<String> array, Context context) {
            m_List = array;
            mContext = context;
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

            ImageView imageView = null;

            if(convertView == null) {
                imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                convertView = imageView;
            }

            Picasso.with(mContext).load(m_List.get(position)).into(imageView);
            return convertView;
        }
    }
}
