package com.pjcstudio.managementpoop.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pjcstudio.managementpoop.Adapter.CalendarDayAdapter;
import com.pjcstudio.managementpoop.DialogFrangment.SelectFixDel;
import com.pjcstudio.managementpoop.DialogFrangment.SelectModeDialog;
import com.pjcstudio.managementpoop.Items.CalendarDayItem;
import com.pjcstudio.managementpoop.Items.CalendarDaySubItem;
import com.pjcstudio.managementpoop.Items.CalendarMonthItem;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.CalendarTime;
import com.pjcstudio.managementpoop.Utility.CalendarUtility;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 7. 27..
 */
public class CalendarDayFragment extends Fragment implements
        PoopRecord.SetPoopRecordListener,
        FoodRecord.SetFoodRecordListener,
        SelectFixDel.SelectFixDelListener {

    private String mSession;
    private int mYear, mMonth, mDay;
    private CalendarDayAdapter mAdapter;
    private ListView mListView;
    private ArrayList<CalendarDayItem> mDayItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("calendar", "day");
        View rootView = inflater.inflate(R.layout.fragment_calendar_day, container, false);
        setInit();
        setLayout(rootView);
        return rootView;
    }

    private void setLayout(View v) {

        mListView = (ListView) v.findViewById(R.id.list_day);
        mListView.setAdapter(mAdapter);
        //mListView.setItemsCanFocus(false);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("listday", "day" + mMonth);
                String str = String.format("%d.%02d.%02d", mYear, mMonth, i+1);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, mYear);
                calendar.set(Calendar.MONTH, mMonth-1);
                calendar.set(Calendar.DAY_OF_MONTH, i+1);
                str = str + " (" + CalendarUtility.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)) + ")";
                ((CalendarFragment) getParentFragment()).setParentTextView(str, 1);

                Bundle arg = new Bundle();
                Log.d("yearmonth", mYear + " : " + i);
                arg.putString("year", String.valueOf(mYear));
                arg.putString("month", String.valueOf(mMonth));
                arg.putString("day", String.valueOf(i+1));
                SelectModeDialog selectModeDialog = new SelectModeDialog();
                selectModeDialog.setArguments(arg);
                selectModeDialog.setTargetFragment(CalendarDayFragment.this, 0);
                selectModeDialog.show(getChildFragmentManager(), "SET");
                getDataMonth(String.valueOf(mYear), String.valueOf(mMonth));
                ((CalendarFragment) getParentFragment()).setListContent(mYear, mMonth, i+1);
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("listday", "day" + mMonth);
                String str = String.format("%d.%02d.%02d", mYear, mMonth, i+1);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, mYear);
                calendar.set(Calendar.MONTH, mMonth-1);
                calendar.set(Calendar.DAY_OF_MONTH, i+1);
                str = str + " (" + CalendarUtility.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)) + ")";
                ((CalendarFragment) getParentFragment()).setParentTextView(str, 1);
                ((CalendarFragment) getParentFragment()).setListContent(mYear, mMonth, i+1);
            }
        });
        getDataMonth(String.valueOf(mYear), String.valueOf(mMonth));
    }

    // 날짜 가져오기
    private void setInit() {
        Calendar calendar = Calendar.getInstance();
        mSession = ((CalendarFragment) getParentFragment()).mSession;
        mYear = ((CalendarFragment) getParentFragment()).toYear;
        mMonth = ((CalendarFragment) getParentFragment()).toMonth;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mAdapter = new CalendarDayAdapter(getActivity());
        mDayItem = new ArrayList<CalendarDayItem>();
    }

    // 달 데이터 가져오기
    private void getDataMonth(String year, String month) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        mDayItem.clear();
        service.getDataToMonth(mSession, year, month, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("CalendarDay", jsonObject.toString());
                    if(jsonObject.getString("code").equals("1")) { // 조회성공이면
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("poo")); // 똥 분석
                        ArrayList<CalendarMonthItem> poop_list = new ArrayList<CalendarMonthItem>();
                        for (int idx = 0; idx < jsonArray.length(); idx++) {
                            JSONObject jsonSub = jsonArray.getJSONObject(idx);
                            CalendarMonthItem calendarMonthItem = new CalendarMonthItem();
                            calendarMonthItem.types = jsonSub.getString("type");
                            calendarMonthItem.year = jsonSub.getString("year");
                            calendarMonthItem.month = jsonSub.getString("month");
                            calendarMonthItem.day = jsonSub.getString("day");
                            calendarMonthItem.hour = jsonSub.getString("hour");
                            calendarMonthItem.minute = jsonSub.getString("minute");
                            calendarMonthItem.types = jsonSub.getString("type");
                            poop_list.add(calendarMonthItem);
                        }
                        jsonArray = new JSONArray(jsonObject.getString("food"));
                        ArrayList<CalendarMonthItem> food_list = new ArrayList<CalendarMonthItem>();
                        for (int idx = 0; idx < jsonArray.length(); idx++) {
                            CalendarMonthItem calendarMonthItem = new CalendarMonthItem();
                            JSONObject jsonSub = jsonArray.getJSONObject(idx);
                            calendarMonthItem.itemtype = 2;
                            calendarMonthItem.year = jsonSub.getString("year");
                            calendarMonthItem.month = jsonSub.getString("month");
                            calendarMonthItem.day = jsonSub.getString("day");
                            calendarMonthItem.hour = jsonSub.getString("hour");
                            calendarMonthItem.minute = jsonSub.getString("minute");
                            calendarMonthItem.types = jsonSub.getString("type");
                            calendarMonthItem.memo = jsonSub.getString("memo");
                            food_list.add(calendarMonthItem);
                        }
                        sortMonthData(poop_list, food_list);

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

    // 가져온 데이터를 일뷰 Adapter 에 맞게 데이터 정렬
    private void sortMonthData(ArrayList<CalendarMonthItem> poop, ArrayList<CalendarMonthItem> food) {
        Calendar calendar = Calendar.getInstance();
        int bYear = calendar.get(Calendar.YEAR);
        int bMonth = calendar.get(Calendar.MONTH)+1;
        int bLastDay = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        int bDay = calendar.get(Calendar.DAY_OF_MONTH);
        Log.d("bLastDay", String.valueOf(bLastDay));
        for(int i=0;i<bLastDay;i++) {
            CalendarDayItem dayItem = new CalendarDayItem();
            dayItem.year = String.valueOf(mYear);
            dayItem.month = String.valueOf(mMonth);
            dayItem.day = String.valueOf(i+1);
            mDayItem.add(dayItem);
        }
        for(int idx=0;idx<poop.size();idx++) {
            Log.d("poopday", poop.get(idx).day);
            CalendarDayItem calendarDayItem = mDayItem.get(Integer.parseInt(poop.get(idx).day)-1); // 맞는날에 데이터 저장
            CalendarDaySubItem calendarDaySubItem = new CalendarDaySubItem();
            int hour = Integer.parseInt(poop.get(idx).hour);
            int minute = Integer.parseInt(poop.get(idx).minute);
            int type = Integer.parseInt(poop.get(idx).types);
            calendarDaySubItem.hour = String.format("%02d", hour);
            calendarDaySubItem.minute = String.format("%02d", minute);
            calendarDaySubItem.type = String.valueOf(type);
            calendarDayItem.List_poop.add(calendarDaySubItem);
        }
        for(int idx=0;idx<food.size();idx++) {
            CalendarDayItem calendarDayItem = mDayItem.get(Integer.parseInt(food.get(idx).day)-1);
            CalendarDaySubItem calendarDaySubItem = new CalendarDaySubItem();
            int hour = Integer.parseInt(food.get(idx).hour);
            int minute = Integer.parseInt(food.get(idx).minute);
            int type = Integer.parseInt(food.get(idx).types);
            calendarDaySubItem.hour = String.format("%02d", hour);
            calendarDaySubItem.minute = String.format("%02d", minute);
            calendarDaySubItem.type = String.valueOf(type);
            calendarDayItem.List_food.add(calendarDaySubItem);
        }
        mAdapter.setListItem(mDayItem);
        mAdapter.notifyDataSetChanged();
        mListView.setSelectionFromTop(mDay-3, 0);
    }

    // 음식등록 하기
    @Override
    public void registerFood(String id, final String year, final String month, final String day, String hour, String minute, String memo, String type, int FixOrNew) {
        Log.d("RegisterPoop", year + " " + month + " " + day + " " + hour + " " + minute + " " + type);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        if(FixOrNew == 1) {
            service.RegisterFood(mSession, type, memo, year, month, day, hour, minute, new Callback<Response>() { // 똥 등록하기
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        Log.d("Regpoopobj", jsonObject.toString());
                        if (jsonObject.getString("code").equals("1")) { // 성공일경우
                            Toast.makeText(getActivity(), "음식 등록 성공!", Toast.LENGTH_SHORT).show();
                            //Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            //mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        } else { // 실패 ( 세션누락 or 인자값문제 )

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
        else if(FixOrNew == 0) {
            service.ModifyFood(mSession, id, type, memo, year, month, day, hour, minute, new Callback<Response>() { // 똥 등록하기
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        Log.d("Regpoopobj", jsonObject.toString());
                        if (jsonObject.getString("code").equals("1")) { // 성공일경우
                            Toast.makeText(getActivity(), "음식수정 성공!", Toast.LENGTH_SHORT).show();
                            //Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            //mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        } else { // 실패 ( 세션누락 or 인자값문제 )
                            Toast.makeText(getActivity(), "음식수정 실패!!", Toast.LENGTH_SHORT).show();

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

    @Override
    public void registerPoop(String id, final String year, final String month, final String day, String hour, String minute, String type, int FixOrNew) {
        Log.d("RegisterPoop", year + " " + month + " " + day + " " + hour + " " + minute + " " + type);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);

        if(FixOrNew == 1) {
            service.RegisterPoo(mSession, type, year, month, day, hour, minute, new Callback<Response>() { // 똥 등록하기
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        Log.d("Regpoopobj", jsonObject.toString());
                        if (jsonObject.getString("code").equals("1")) { // 성공일경우
                            Toast.makeText(getActivity(), "응가 등록 성공!", Toast.LENGTH_SHORT).show();
                            //Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            //mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                            getDataMonth(String.valueOf(mYear), String.valueOf(mMonth));
                        } else { // 실패 ( 세션누락 or 인자값문제 )
                            Toast.makeText(getActivity(), "응가 등록 실패!", Toast.LENGTH_SHORT).show();
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
        else if(FixOrNew == 0) {
            service.ModifyPoo(mSession, id, type, year, month, day, hour, minute, new Callback<Response>() { // 똥 등록하기
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        Log.d("Regpoopobj", jsonObject.toString());
                        if (jsonObject.getString("code").equals("1")) { // 성공일경우
                            Toast.makeText(getActivity(), "똥수정 성공!", Toast.LENGTH_SHORT).show();
                            //Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            //mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                            getDataMonth(String.valueOf(mYear), String.valueOf(mMonth));
                        } else { // 실패 ( 세션누락 or 인자값문제 )
                            Toast.makeText(getActivity(), "똥수정 실패!!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void deleteItem(String type, String itemId, String day) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);

        if (type.equals("poop")) {
            service.DeletePoo(mSession, itemId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        if (jsonObject.getString("code").equals("1")) {
                            Toast.makeText(getActivity(), "삭제성공", Toast.LENGTH_SHORT).show();
                            getDataMonth(String.valueOf(mYear), String.valueOf(mMonth));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "삭제실패", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (type.equals("food")) {
            service.DeleteFood(mSession, itemId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Toast.makeText(getActivity(), "삭제성공", Toast.LENGTH_SHORT).show();
                    getDataMonth(String.valueOf(mYear), String.valueOf(mMonth));
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "삭제실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
        int retn = ((CalendarFragment) getParentFragment()).deleteListItem(1);
    }
}
