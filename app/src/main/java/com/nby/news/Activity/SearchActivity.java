package com.nby.news.Activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nby.news.Adapter.HintListAdapter;
import com.nby.news.Adapter.HistoryRecAdapter;
import com.nby.news.R;
import com.nby.news.db.DBHelper;
import com.nby.news.model.HotSportModel;
import com.nby.news.unit.SharedPreferencesUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends Activity{

    private RecyclerView recyclerView;
    private HotSportModel hotSportModel;
    private ImageView search_icon;
    private EditText searchEditTextView;
    private HistoryRecAdapter historyRecAdapter;
    private HintListAdapter hintListAdapter;
    private ListView listView;
    private DBHelper dbHelper;
    private Map<String, String> hints = new HashMap<>();
    private List<String> historyList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        setContentView(R.layout.activity_search_layout);
        dbHelper = new DBHelper(getApplicationContext()
                ,"DBHelper",null,1);
        if(!dbHelper.IsTableExist("history",dbHelper.getWritableDatabase())){
            dbHelper.createTable("create table history(title text)");
        }else{
            historyList = dbHelper.getHistoryList("history",new String[]{"title"}
            ,"title","");
        }
        //初始化控件
        listView = findViewById(R.id.hintList_search);
        search_icon = findViewById(R.id.search_activity_icon);
        searchEditTextView = findViewById(R.id.search_activity_input);
        recyclerView = findViewById(R.id.search_activity_history_recycler);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);
        historyRecAdapter = new HistoryRecAdapter(this,historyList);
        recyclerView.setAdapter(historyRecAdapter);


        //获取搜索数据源
        SharedPreferencesUnit spUnit = new SharedPreferencesUnit(this);
        spUnit.getNewsBean("News");
        search_icon.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                addOneHistoryRecord();
            }
        });
        //设置监听
        searchEditTextView.setOnKeyListener(new View.OnKeyListener( ) {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    Log.e("监听到enter"," ");
//                    Intent intent = new Intent("android.intent.action.SEARCH");
//                    intent.putExtra(SearchManager.QUERY,searchEditTextView.getText());
//                    startActivity(intent);
                    addOneHistoryRecord();
                    return true;
                }
                return false;
            }
        });
        searchEditTextView.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String ss = s.toString();
                hints.clear();
                Log.e("Text变化内容",ss);
                if(ss.equals("")){
                    showList();
                    return;
                }
                dbHelper.queryDate("search",new String[]{"title","url"},"title",ss);
                hints = dbHelper.getDataMap("search",new String[]{"title"},"title",ss);
                showList();
            }
        });
    }

    public void addOneHistoryRecord(){
        String text = searchEditTextView.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", text);
        dbHelper.getWritableDatabase()
                .insert("history",null,contentValues);
        historyList.add(text);
        historyRecAdapter.notifyDataSetChanged();
    }

    public void showList(){
        List<String> list = new ArrayList<>(hints.keySet());
        hintListAdapter = new HintListAdapter(this,list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectText = list.get(position);
                searchEditTextView.setText(selectText);
                Toast.makeText(getApplicationContext(),"url:"+hints.get(selectText)
                        ,Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(hintListAdapter);
    }




    @Override
    protected void onNewIntent(Intent intent) {
        Log.e("onNewIntent"," ");
//        setIntent(intent);
//        handleIntent(intent);
    }

    public void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.e("正在搜索内容",query);
            doMySearch(query);
        }
    }

    public void doMySearch(String s){
        //这里进行搜索操作
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
