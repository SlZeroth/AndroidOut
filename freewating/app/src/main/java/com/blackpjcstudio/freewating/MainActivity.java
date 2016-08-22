package com.blackpjcstudio.freewating;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blackpjcstudio.freewating.Items.BoardItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ListView.OnScrollListener {

    private ListView boardList;
    private BoardAdapter adapter;
    private int currentBoardIdx = 1;
    private boolean lastItemVisibleFlag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setQueryHint("게임 이름을 입력하세요.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new SelectBoard(0, query).start();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void init() {
        boardList = (ListView) findViewById(R.id.boardlist);
        adapter = new BoardAdapter(this);
        boardList.setAdapter(adapter);
        boardList.setOnScrollListener(this);
        boardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BoardItem currentItem = (BoardItem) parent.getAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, CastInfomationActivity.class);
                intent.putExtra("Link", currentItem.castLink);
                intent.putExtra("Title", currentItem.castName);
                startActivity(intent);
            }
        });
        new SelectBoard(1, "0").start();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
            currentBoardIdx++;
            new SelectBoard(1, "0").start();
            Log.d("onScroll", "Bottom");
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(view.getAdapter().getCount() != 0) {
            lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
        }
    }

    private class SelectBoard extends Thread {

        private int selectType;
        private String searchStr;
        private ProgressDialog mProgressDialog;

        static final int TYPE_SEARCH = 0;
        static final int TYPE_DEFAULT = 1;

        public SelectBoard(int type, String str) {
            type = selectType;
            searchStr = str;
        }

        @Override
        public void run() {

            try {

                showProgress();

                Document doc = null;
                    doc = Jsoup.connect("http://baykoreans.net/index.php?mid=drama&page=" + currentBoardIdx).get();
                    Log.d("text", doc.text());
                Elements table = doc.select("TABLE[class=boardList webZine]");
                Elements contents2 = table.select("tbody");
                Elements tr = contents2.select("tr");
                Log.d("tr", tr.get(0).text());

                for(int i=0;i<tr.size();i++) {

                    Element current = tr.get(i);
                    BoardItem item = new BoardItem();
                    Elements link = current.select("TD[class=title]");
                    String linkstr = link.select("DIV[class=thumbnailbox]").select("A").attr("href");
                    Log.d("link", linkstr);
                    item.castName = current.select("TD[class=title]").text();
                    item.castName = item.castName.replace("reader", "");
                    item.castName = item.castName.replace("rating", "");
                    item.castLink = linkstr;
                    adapter.addItem(item);
                }

                exitProgress();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void showProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog = ProgressDialog.show(MainActivity.this, "", "잠시만 기다려주세요.", true);
                }
            });

        }

        private void exitProgress() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
        }
    }
}
