package com.taeyangsangsa.pickupusedoil.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.taeyangsangsa.pickupusedoil.Adapter.BoardAdapter;
import com.taeyangsangsa.pickupusedoil.DialogFragment.SugoDialogFragment;
import com.taeyangsangsa.pickupusedoil.Items.BoardItem;
import com.taeyangsangsa.pickupusedoil.MainActivity;
import com.taeyangsangsa.pickupusedoil.R;
import com.taeyangsangsa.pickupusedoil.Tools.NetworkService;
import com.taeyangsangsa.pickupusedoil.Utility.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.ConsoleHandler;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

/**
 * Created by juchanpark on 2015. 8. 25..
 */
public class BoardFragment extends Fragment {

    private ListView boardList;
    private BoardAdapter boardAdapter;
    private ImageButton btn_call, btn_3, btn_2;

    static final String SUGO_YES = "2";
    static final String SUGO_NO = "1";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_board, container, false);
        ((MainActivity)getActivity()).setActionbarTitle(0);
        setLayout(rootView);
        getBoardItem();
        return rootView;
    }

    private void setLayout(View v) {
        boardAdapter = new BoardAdapter(getActivity());
        boardList = (ListView) v.findViewById(R.id.listview_board);
        boardList.setAdapter(boardAdapter);
        btn_call = (ImageButton) v.findViewById(R.id.board_call);
        btn_3 = (ImageButton) v.findViewById(R.id.board_3);
        btn_2 = (ImageButton) v.findViewById(R.id.board_2);
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:1599-5182"));
                startActivity(intent);
            }
        });
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).movePage(2);
            }
        });
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).movePage(1);
            }
        });
        ((ImageButton) v.findViewById(R.id.board_1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).movePage(0);
            }
        });

        boardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if(((MainActivity)getActivity()).isUser.equals("Y")) {
                    BoardItem clickItem = (BoardItem) adapterView.getAdapter().getItem(position);
                    if (clickItem.sugoflag.equals(SUGO_YES)) {
                        Toast.makeText(getActivity(), "이미 수거완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    } else if (clickItem.sugoflag.equals(SUGO_NO)) {
                        SugoDialogFragment dialog = new SugoDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("itemNum", clickItem.seq);
                        dialog.setTargetFragment(BoardFragment.this, 0);
                        dialog.setArguments(bundle);
                        dialog.show(getChildFragmentManager(), "SUGO");
                    }
                }
            }
        });
    }

    private void getBoardItem() {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://bizhyper.ddns.net:9235")
                .build();

        Log.d("phone", MainActivity.phoneNum);
        NetworkService service = restAdapter.create(NetworkService.class);
        service.Json_Sugo(((MainActivity)getActivity()).isUser, MainActivity.phoneNum, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    int bYear = 0, bMonth = 0, first = 1;
                    for(int idx=0;idx<jsonArray.length();idx++) {
                        String editStr = "";
                        BoardItem item = new BoardItem();
                        BoardItem item2 = new BoardItem();
                        JSONObject jsonObject = jsonArray.getJSONObject(idx);
                        item.viewtype = 1;
                        item.year = jsonObject.getString("year");
                        item2.year = jsonObject.getString("year");
                        item.month = jsonObject.getString("month");
                        item2.month = jsonObject.getString("month");
                        item.day = jsonObject.getString ("day");
                        item.companyName = jsonObject.getString("companyname");
                        item.companyPhoneNumber = jsonObject.getString("companyphone");
                        item.companyAddress = jsonObject.getString("companyaddr");
                        item.comment = jsonObject.getString("comment");
                        item.getday = jsonObject.getString("companydate");
                        item.seq = jsonObject.getString("seq");
                        item.sugocompany = jsonObject.getString("sugocompany");
                        item.sugoflag = jsonObject.getString("sugoflag");
                        if(((MainActivity)getActivity()).isUser.equals("N")) {
                            Log.d("ddd",item.companyName);
                            Log.d("ddd",item.companyName);
                            Log.d("ddd",item.companyName);

                            if(((MainActivity) getActivity()).isUser.equals("N")) {
                                Log.d("len", item.companyName.length()+"");
                                if(item.companyName.length() >= 2) {
                                    Log.d("aaaa", "a");
                                    char firstChar = item.companyName.charAt(0);
                                    char secondChar = item.companyName.charAt(1);
                                    editStr += Character.toString(firstChar);
                                    editStr += Character.toString(secondChar);
                                    item.companyName = item.companyName.replace(editStr, "**");
                                }

                                if(item.companyPhoneNumber.length() >= 8) {
                                    Log.d("aaaa", "aa");
                                    String subPhone = item.companyPhoneNumber.substring(4, 7);
                                    item.companyPhoneNumber = item.companyPhoneNumber.replace(subPhone, "****");
                                }

                                if(item.companyAddress.length() >= 2) {
                                    Log.d("aaaa", "aaa");
                                    String subAddress = item.companyAddress.substring(0, 2);
                                    item.companyAddress = item.companyAddress.replace(subAddress, "****");
                                }
                            }
                        }

                        if((bMonth != Integer.parseInt(item.month))) {
                            item2.viewtype = 0;
                            boardAdapter.addItem(item2);
                        }
                        bMonth = Integer.parseInt(item.month);
                        boardAdapter.addItem(item);
                    }
                    boardAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });
    }

    public void sugoYesDialog(String seq) {

        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), "", "잠시만 기다려주세요.", true);

        Util.getNetworkService().Json_UpdateSugo(seq, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    Log.d("UpdateSUGO", jsonArray.toString());
                    if (jsonArray.length() == 1) {
                        JSONObject jsonObj = jsonArray.getJSONObject(0);
                        if (jsonObj.getString("return").equals("Y")) {
                            Toast.makeText(getActivity(), "수거 완료했습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "수거 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
            }
        });
    }
}
