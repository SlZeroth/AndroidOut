package com.pjcstudio.managementpoop.Fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pjcstudio.managementpoop.Activity.MainActivity;
import com.pjcstudio.managementpoop.Activity.SettingActivity;
import com.pjcstudio.managementpoop.Adapter.CalendarDayInfoAdapter;
import com.pjcstudio.managementpoop.DialogFrangment.SelectFixDel;
import com.pjcstudio.managementpoop.Items.CalendarDayInfoItem;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.CalendarUtility;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.Callback;

/**
 * Created by juchanpark on 2015. 7. 7..
 */
public class CalendarFragment extends Fragment implements
        View.OnClickListener {

    public String mSession;
    public int toYear, toMonth;
    private LinearLayout mLinearLine;
    private TextView mTextYear, mTextBottomYear;
    private Fragment fragmentMonth, fragmentDay;
    private ListView mListView;
    private ImageButton setting;
    private CalendarDayInfoAdapter mListAdapter;
    private ArrayList<CalendarDayInfoItem> mInfoItem;
    private Button mBtnDay, mBtnMonth;
    private FragmentManager fm;
    private static View saveView = null;
    private int changeClick;
    private int ListCurrentClick = 0; // 사용자가 누른 리스트 열번호
    private String selectItem;
    private Typeface typeface;


    static final int CHANGE_DAYVIEW = 0;
    static final int CHANGE_MONTHVIEW = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(saveView == null) {
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);
            setInit();
            setLayout(rootView);
            saveView = rootView;
            return rootView;
        } else {
            Log.d("....", ".....");
            return saveView;
        }
    }

    @Override
    public void onPause() {
        Log.d("CalendarFragment", "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("CalendarFragment", "onStop");
        super.onStop();
    }

    private void setInit() {
        typeface = ((MainActivity)getActivity()).typeface;
        Calendar calendar = Calendar.getInstance();
        toYear = calendar.get(Calendar.YEAR);
        toMonth = calendar.get(Calendar.MONTH) + 1;
        mSession = ((MainActivity)getActivity()).session;
        changeClick = CHANGE_MONTHVIEW;
        mInfoItem = new ArrayList<CalendarDayInfoItem>();
        Log.d("Session", mSession);
        fragmentMonth = new CalendarMonthFragment();
        fragmentDay = new CalendarDayFragment();
        fm = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.calendar_fragment, fragmentMonth);
        fragmentTransaction.commit();
    }

    private void setLayout(View v) {
        mLinearLine = (LinearLayout) v.findViewById(R.id.linearsevenday);
        mTextYear = (TextView) v.findViewById(R.id.month_yeartext);
        mTextYear.setTypeface(typeface);
        mTextBottomYear = (TextView) v.findViewById(R.id.calendarinfo_day);
        mListView = (ListView) v.findViewById(R.id.listviewdayinfo);
        setting = (ImageButton) v.findViewById(R.id.settingbtn);
        setting.setOnClickListener(this);
        mListAdapter = new CalendarDayInfoAdapter(getActivity());
        mListView.setAdapter(mListAdapter);
        mBtnDay = (Button) v.findViewById(R.id.changeday);
        mBtnDay.setOnClickListener(this);
        mBtnDay.setSelected(false);
        mBtnMonth = (Button) v.findViewById(R.id.changemonth);
        mBtnMonth.setOnClickListener(this);
        mBtnMonth.setSelected(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectItem = mInfoItem.get(position).itemid;
                SelectFixDel dialog = new SelectFixDel();
                Bundle bundles = new Bundle();
                bundles.putString("year", mInfoItem.get(position).year);
                bundles.putString("month", mInfoItem.get(position).month);
                bundles.putString("day", mInfoItem.get(position).day);
                bundles.putString("id", mInfoItem.get(position).itemid);
                if(mInfoItem.get(position).itemtype.equals("P")) {
                    bundles.putString("types", "poop");
                }
                else if(mInfoItem.get(position).itemtype.equals("F")) {
                    bundles.putString("types", "food");
                }
                dialog.setArguments(bundles);
                if(changeClick == CHANGE_MONTHVIEW) {
                    dialog.setTargetFragment(fragmentMonth, 0);
                } else if(changeClick == CHANGE_DAYVIEW) {
                    dialog.setTargetFragment(fragmentDay, 0);
                }
                dialog.show(getChildFragmentManager(), "ST");
                ListCurrentClick = position;
                return false;
            }
        });
        Calendar calendar = Calendar.getInstance();
        setListContent(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
        int aYear = calendar.get(Calendar.YEAR);
        int aMonth = calendar.get(Calendar.MONTH)+1;
        int aDay = calendar.get(Calendar.DAY_OF_MONTH);
        String str = String.format("%d.%02d.%02d", aYear, aMonth, aDay);
        str = str + " (" + CalendarUtility.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)) + ")";
        mTextBottomYear.setText(str);
    }

    public int deleteListItem(int itemNum) {
        int retnValue = 0;
        if(mListAdapter.dropItem(ListCurrentClick) == 0) {
            retnValue = 1;
            //currentInfo.calendarView.CalendarFixday(currentInfo.day, 0);
        }
        mListAdapter.notifyDataSetChanged();
        return retnValue;
    }

    public void setParentTextView(String year, int viewType) {
        if(viewType == 0) {
            mTextYear.setText(year);
        }
        else if(viewType == 1) {
            mTextBottomYear.setText(year);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.changeday:
                if (changeClick == CHANGE_MONTHVIEW) {
                    mBtnDay.setBackgroundResource(R.drawable.ico_day);
                    mBtnMonth.setBackgroundResource(R.drawable.ico_month_off);

                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.calendar_fragment, fragmentDay);
                    fragmentTransaction.commit();
                    mLinearLine.setVisibility(View.GONE);
                    changeClick = CHANGE_DAYVIEW;
                }
                break;
            case R.id.changemonth:
                if (changeClick == CHANGE_DAYVIEW) {
                    mBtnDay.setBackgroundResource(R.drawable.ico_day_off);
                    mBtnMonth.setBackgroundResource(R.drawable.ico_month);
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.calendar_fragment, fragmentMonth);
                    fragmentTransaction.commit();
                    mLinearLine.setVisibility(View.VISIBLE);
                    changeClick = CHANGE_MONTHVIEW;
                }
                break;
            case R.id.settingbtn:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
        }
    }

    public void setListContent(int year, int month, int day) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(5, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(okHttpClient))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        service.getDataToDay(mSession, String.valueOf(year), String.valueOf(month), String.valueOf(day), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    if(jsonObject.getString("code").equals("1")) {
                        mInfoItem.clear();
                        mListAdapter.notifyDataSetInvalidated();
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("poo")); // 음식
                        for(int idx=0;idx<jsonArray.length();idx++) {
                            JSONObject jsonSub = jsonArray.getJSONObject(idx);
                            Calendar timeCal = Calendar.getInstance();
                            CalendarDayInfoItem calendarDayInfoItem = new CalendarDayInfoItem();
                            calendarDayInfoItem.year = jsonSub.getString("year");
                            calendarDayInfoItem.month = jsonSub.getString("month");
                            calendarDayInfoItem.day = jsonSub.getString("day");
                            calendarDayInfoItem.hour = jsonSub.getString("hour");
                            calendarDayInfoItem.minute = jsonSub.getString("minute");
                            calendarDayInfoItem.type = jsonSub.getString("type");
                            calendarDayInfoItem.itemid = jsonSub.getString("id");
                            calendarDayInfoItem.itemtype = "P";
                            timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(calendarDayInfoItem.hour));
                            timeCal.set(Calendar.MINUTE, Integer.parseInt(calendarDayInfoItem.minute));
                            Date timeDate = timeCal.getTime();
                            calendarDayInfoItem.time = timeDate.getTime();
                            mInfoItem.add(calendarDayInfoItem);
                        }
                        jsonArray = new JSONArray(jsonObject.getString("food"));
                        for(int idx=0;idx<jsonArray.length();idx++) {
                            JSONObject jsonSub = jsonArray.getJSONObject(idx);
                            Calendar timeCal = Calendar.getInstance();
                            CalendarDayInfoItem calendarDayInfoItem = new CalendarDayInfoItem();
                            calendarDayInfoItem.year = jsonSub.getString("year");
                            calendarDayInfoItem.month = jsonSub.getString("month");
                            calendarDayInfoItem.day = jsonSub.getString("day");
                            calendarDayInfoItem.hour = jsonSub.getString("hour");
                            calendarDayInfoItem.minute = jsonSub.getString("minute");
                            calendarDayInfoItem.type = jsonSub.getString("type");
                            calendarDayInfoItem.memo = jsonSub.getString("memo");
                            calendarDayInfoItem.itemid = jsonSub.getString("id");
                            calendarDayInfoItem.itemtype = "F";
                            timeCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(calendarDayInfoItem.hour));
                            timeCal.set(Calendar.MINUTE, Integer.parseInt(calendarDayInfoItem.minute));
                            Date timeDate = timeCal.getTime();
                            calendarDayInfoItem.time = timeDate.getTime();
                            mInfoItem.add(calendarDayInfoItem);
                        }
                        for(int i=0;i<mInfoItem.size();i++) {
                            Log.d("mList", mInfoItem.get(i).itemtype + mInfoItem.get(i).type + " " + mInfoItem.get(i).time);
                        }
                        List<CalendarDayInfoItem> arList = mInfoItem;
                        Collections.sort(arList, new Comparator<CalendarDayInfoItem>() {
                            @Override
                            public int compare(CalendarDayInfoItem calendarDayInfoItem, CalendarDayInfoItem calendarDayInfoItem2) {
                                return calendarDayInfoItem.time > calendarDayInfoItem2.time ? -1 : calendarDayInfoItem.time < calendarDayInfoItem2.time ? 1:0;
                            }
                        });
                        Log.d("arList", arList.toString());
                        mInfoItem = new ArrayList<CalendarDayInfoItem>(arList);

                        for(int i=1;i<mInfoItem.size();i++) {
                            if(mInfoItem.get(i).time == mInfoItem.get(i-1).time) {
                                mInfoItem.get(i).sametime = 1;
                            } else {
                                mInfoItem.get(i).sametime = 0;
                            }
                        }
                        mListAdapter.setItemlist(mInfoItem);
                        mListAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
