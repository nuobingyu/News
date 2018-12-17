package com.nby.news.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
    private TextView clearAllHistoryText;
    private EditText searchEditTextView;
    private HistoryRecAdapter historyRecAdapter;
    private HintListAdapter hintListAdapter;
    private ListView listView;
    private DBHelper dbHelper;
    private Map<String, String> hints = new HashMap<>();
    private List<String> historyList = new ArrayList<>();
    private Context mContext = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        clearAllHistoryText = findViewById(R.id.clear_history_text);
        listView = findViewById(R.id.hintList_search);
        search_icon = findViewById(R.id.search_activity_icon);
        searchEditTextView = findViewById(R.id.search_activity_input);
        recyclerView = findViewById(R.id.search_activity_history_recycler);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(manager);
        historyRecAdapter = new HistoryRecAdapter(this, historyList, new HistoryRecAdapter.ClickListener( ) {
            @Override
            public void onClick(View v, int position) {
                searchEditTextView.setText(((TextView)v).getText());
            }

            @Override
            public void onLongClick(View v) {
                TextView title = (TextView) v;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
                dialogBuilder.setTitle("");
                dialogBuilder.setMessage("您是否要删除 “"+title.getText()+"” 这条历史记录");
                dialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteDate("history","title"
                                ,new String[]{title.getText().toString()});
                        historyRecAdapter.notifyDataSetChanged();
                    }
                });
                dialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener( ) {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialogBuilder.show();
            }
        });
        recyclerView.setAdapter(historyRecAdapter);
        if(historyList.size()<=0){
            clearAllHistoryText.setVisibility(View.GONE);
        }else{
            clearAllHistoryText.setVisibility(View.VISIBLE);
        }

        //获取搜索数据源
        SharedPreferencesUnit spUnit = new SharedPreferencesUnit(this);
        spUnit.getNewsBean("News");
        clearAllHistoryText.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                dbHelper.deleteTable("history");
                dbHelper.createTable("create table history(title text)");
            }
        });
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

}
