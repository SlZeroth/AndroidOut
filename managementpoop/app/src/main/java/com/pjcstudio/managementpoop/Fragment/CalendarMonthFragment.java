package com.pjcstudio.managementpoop.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pjcstudio.managementpoop.Activity.MainActivity;
import com.pjcstudio.managementpoop.Adapter.CalendarDayInfoAdapter;
import com.pjcstudio.managementpoop.DialogFrangment.SelectFixDel;
import com.pjcstudio.managementpoop.DialogFrangment.SelectModeDialog;
import com.pjcstudio.managementpoop.DialogFrangment.SetFoodRecordDialog;
import com.pjcstudio.managementpoop.DialogFrangment.SetPoopRecordDialog;
import com.pjcstudio.managementpoop.DialogFrangment.SwitchingModeDialog;
import com.pjcstudio.managementpoop.Items.CalendarDayInfoItem;
import com.pjcstudio.managementpoop.Items.CalendarMonthItem;
import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.CalendarTime;
import com.pjcstudio.managementpoop.Utility.CalendarUtility;
import com.pjcstudio.managementpoop.Utility.NetworkService;
import com.pjcstudio.managementpoop.Utility.ServerInfo;
import com.pjcstudio.managementpoop.Views.CalendarView;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 7. 27..
 */
public class CalendarMonthFragment extends Fragment implements
        PoopRecord.SetPoopRecordListener,
        FoodRecord.SetFoodRecordListener,
        SelectFixDel.SelectFixDelListener {

    private String mSession;
    private ViewPager mViewpager;
    private PagerAdapter mPageAdapter;
    private ArrayList<CalendarMonthItem> mMonthItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("calendar", "month");
        View rootView = inflater.inflate(R.layout.fragment_calendar_month, container, false);
        setInit();
        setLayout(rootView);
        return rootView;
    }

    private void setInit() {
        mSession = ((CalendarFragment) getParentFragment()).mSession;
        mMonthItem = new ArrayList<CalendarMonthItem>();
    }

    private void setLayout(View v) {
        mViewpager = (ViewPager) v.findViewById(R.id.month_pager);
        mViewpager.setOffscreenPageLimit(1);
        mPageAdapter = new MonthPagerAdapter(getActivity());
        mViewpager.setAdapter(mPageAdapter);
        mViewpager.setCurrentItem(50000);

        mViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Calendar calendar = Calendar.getInstance();
                int month = 0;
                if(position == 50000) {
                    month = 0;
                }
                else if(position < 50000) {
                    month = -(50000 - position);
                } else if(position > 50000) {
                    month = +(position - 50000);
                }
                calendar.add(Calendar.MONTH, month);
                String str = String.format("%04d.%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1);
                ((CalendarFragment) getParentFragment()).setParentTextView(str, 0);
            }

            @Override
            public void onPageSelected(int position) {
                Calendar calendar = Calendar.getInstance();
                int month = 0;
                if(position == 50000) {
                    month = 0;
                }
                else if(position < 50000) {
                    month = -(50000 - position);
                } else if(position > 50000) {
                    month = +(position - 50000);
                }
                calendar.add(Calendar.MONTH, month);
                ((CalendarFragment) getParentFragment()).toYear = calendar.get(Calendar.YEAR);
                ((CalendarFragment) getParentFragment()).toMonth = calendar.get(Calendar.MONTH) +1;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("state", "state : " + state);
            }
        });
    }

    public void InitMonthCalendar(CalendarView calendarView, int month) {
        if(mMonthItem.size() > 0) {
            for(int i=0;i<mMonthItem.size();i++) {
                if(mMonthItem.get(i).itemtype == 1) { // 똥이면
                    CalendarMonthItem item = mMonthItem.get(i);
                    calendarView.CalendarFixday(Integer.parseInt(item.day), 1, "P", Integer.parseInt(item.types));
                }
                else if(mMonthItem.get(i).itemtype == 2) { // 음식이면
                    CalendarMonthItem item = mMonthItem.get(i);
                    calendarView.CalendarFixday(Integer.parseInt(item.day), 1, "F", Integer.parseInt(item.types));
                }
            }
        }
    }

    public void InitMonth(final CalendarView calendarView, final int year, final int month) {
        Log.d("InitMonth", "Year : " + year + " Month : " + (month+1));
        final int month_s = month;

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(10, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(okHttpClient))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);

        service.getDataToMonth(mSession, String.valueOf(year), String.valueOf(month+1), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    if(jsonObject.getString("code").equals("1")) { // 조회성공이면
                        mMonthItem.clear();
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("poo")); // 똥 분석
                        for(int idx=0;idx<jsonArray.length();idx++) {
                            CalendarMonthItem calendarMonthItem = new CalendarMonthItem();
                            JSONObject jsonSub = jsonArray.getJSONObject(idx);
                            calendarMonthItem.itemtype = 1;
                            calendarMonthItem.year = jsonSub.getString("year");
                            calendarMonthItem.month = jsonSub.getString("month");
                            calendarMonthItem.day = jsonSub.getString("day");
                            calendarMonthItem.hour = jsonSub.getString("hour");
                            calendarMonthItem.minute = jsonSub.getString("minute");
                            calendarMonthItem.types = jsonSub.getString("type");
                            mMonthItem.add(calendarMonthItem);
                        }
                        jsonArray = new JSONArray(jsonObject.getString("food"));
                        for(int idx=0;idx<jsonArray.length();idx++) {
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
                            mMonthItem.add(calendarMonthItem);
                        }
                        InitMonthCalendar(calendarView, month_s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                //Toast.makeText(getActivity(), "인터넷 접속실패", Toast.LENGTH_SHORT).show();
                Log.e("CalendarMonthFragment","error : "+error.getMessage());
                error.printStackTrace();
                //InitMonthClass(calendarView, year, month);
            }
        });

    }

    @Override
    public void registerFood(String id, final String year, final String month, final String day, String hour, String minute, String memo, String type, int FixOrNew) {
        Log.d("RegisterPoop", year + " " + month + " " + day + " " + hour + " " + minute + " " + type);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
        if(FixOrNew == 1) {
            service.RegisterFood(mSession, type, memo, year, month, day, hour, minute, new Callback<Response>() { // 똥 등록하기
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        Log.d("Regpoopobj", jsonObject.toString());
                        if (jsonObject.getString("code").equals("1")) { // 성공일경우
                            Toast.makeText(getActivity(), "음식등록 성공!", Toast.LENGTH_SHORT).show();
                            Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        } else { // 실패 ( 세션누락 or 인자값문제 )
                            Toast.makeText(getActivity(), "음식등록 실패!!", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
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
                            Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        } else { // 실패 ( 세션누락 or 인자값문제 )
                            Toast.makeText(getActivity(), "음식수정 실패!!", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
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
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);

        if(FixOrNew == 1) {
            service.RegisterPoo(mSession, type, year, month, day, hour, minute, new Callback<Response>() { // 똥 등록하기
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        Log.d("Regpoopobj", jsonObject.toString());
                        if (jsonObject.getString("code").equals("1")) { // 성공일경우
                            Toast.makeText(getActivity(), "응가 등록 성공!", Toast.LENGTH_SHORT).show();
                            Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        } else { // 실패 ( 세션누락 or 인자값문제 )
                            Toast.makeText(getActivity(), "응가 등록 실패!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
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
                            Log.d("ITEMNUM", String.valueOf(mViewpager.getCurrentItem()));
                            //InitMonthClass((CalendarView) viewPagermonth.getChildAt(viewPagermonth.getCurrentItem()), Integer.parseInt(year), Integer.parseInt(month)-1);
                            mPageAdapter.notifyDataSetChanged();
                            ((CalendarFragment) getParentFragment()).setListContent(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
                        } else { // 실패 ( 세션누락 or 인자값문제 )
                            Toast.makeText(getActivity(), "똥수정 실패!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    progressDialog.dismiss();
                }
            });
        }
    }

    @Override
    public void deleteItem(String type, String itemId, final String day) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ServerInfo.APIADDR)
                .setClient(new OkClient(new OkHttpClient()))
                .build();
        NetworkService service = restAdapter.create(NetworkService.class);
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);

        if(type.equals("poop")) {
            service.DeletePoo(mSession, itemId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    try {
                        JSONObject jsonObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                        if(jsonObject.getString("code").equals("1")) {
                            Toast.makeText(getActivity(), "삭제성공", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "삭제실패", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        else if(type.equals("food")) {
            service.DeleteFood(mSession, itemId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Toast.makeText(getActivity(), "삭제성공", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "삭제실패", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
        int retn = ((CalendarFragment) getParentFragment()).deleteListItem(1);
        if(retn == 1) {
            CalendarView calendarView = (CalendarView) mViewpager.findViewWithTag(mViewpager.getCurrentItem());
            calendarView.CalendarFixday(Integer.parseInt(day), 0, "d", 1);
        }
    }

    public class MonthPagerAdapter extends PagerAdapter implements CalendarView.CalendarViewListener { // 캘린더 이동

        private LayoutInflater mInflater;
        private Context mContext;
        private int mYear;

        public MonthPagerAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public int getCount() {
            return 100000;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            int year = 0;
            int month = 0;
            Calendar calendar = Calendar.getInstance();

            if(position == 50000) { // 초기상태일경우 0 으로 지정하면 현재 년,달,
                year = 0;
                month = 0;
            }
            else if(position < 50000) { // 왼쪽으로 스크롤할경우
                month = -(50000 - position);
            } else if(position > 50000) { // 오른쪽으로 스크롤할경우
                month = +(position - 50000);
            }

            calendar.add(Calendar.YEAR, year);
            calendar.add(Calendar.MONTH, month);
            calendar.set(Calendar.DATE, 1);

            CalendarView calendarView = new CalendarView(mContext);
            calendarView.setTag(position);
            calendarView.setClickListener(this);
            calendarView.InitCalendar(year,month); // 인자로준 YEAR, MONTH 로 캘린더 생성
            InitMonth(calendarView, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
            if(position == 50000) {
               // currentInfo.calendarView = calendarView;
            }
            ((ViewPager) container).addView(calendarView, 0);
            return calendarView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int DayClickListener(int year, int month, int day) { // 아래 리스트뷰 아이템 초기화하고 등록
            Log.i("CalendarListener", "YEAR : " + year + " month : " + month + " day : " + day);
            ((CalendarFragment) getParentFragment()).setListContent(year, month, day);
            String str = String.format("%d.%02d.%02d", year, month, day);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            str = str + " (" + CalendarUtility.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)) + ")";
            ((CalendarFragment) getParentFragment()).setParentTextView(str, 1);
            /*
            Bundle arg = new Bundle();
            arg.putString("mode", "food");
            SwitchingModeDialog dialog = new SwitchingModeDialog();
            dialog.setArguments(arg);
            dialog.show(getFragmentManager(), "sw");
            */
            return 1;

        }

        @Override
        public int DayLongClickListener(int year, int month, int day) {
            Log.i("CalendarListener", "YEAR : " + year + " month : " + month + " day : " + day);
            ((CalendarFragment) getParentFragment()).setListContent(year, month, day);
            String str = String.format("%d.%02d.%02d", year, month, day);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month-1);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            str = str + " (" + CalendarUtility.getWeekDay(calendar.get(Calendar.DAY_OF_WEEK)) + ")";
            ((CalendarFragment) getParentFragment()).setParentTextView(str, 1);

            Bundle arg = new Bundle();
            arg.putString("year", String.valueOf(year));
            arg.putString("month", String.valueOf(month));
            arg.putString("day", String.valueOf(day));
            SelectModeDialog selectModeDialog = new SelectModeDialog();
            selectModeDialog.setArguments(arg);
            selectModeDialog.setTargetFragment(CalendarMonthFragment.this, 0);
            selectModeDialog.show(getChildFragmentManager(), "SET");

            /*
            Bundle arg = new Bundle();
            arg.putString("mode", "food");
            SwitchingModeDialog dialog = new SwitchingModeDialog();
            dialog.setArguments(arg);
            dialog.show(getFragmentManager(), "sw");
            */
            return 0;
        }
    }
}
