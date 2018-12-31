package com.nby.news.Activity;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nby.news.Adapter.HintListAdapter;
import com.nby.news.Adapter.HistoryRecAdapter;
import com.nby.news.Interface.DBFinishedInterface;
import com.nby.news.R;
import com.nby.news.db.DBDataBean;
import com.nby.news.db.DBHelper;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchActivity extends Activity{

    private RecyclerView recyclerView;
    private ImageView search_icon;
    private TextView clearAllHistoryText;
    private EditText searchEditTextView;
    private HistoryRecAdapter historyRecAdapter;
    private HintListAdapter hintListAdapter;
    private ListView listView;
    private DBHelper dbHelper;
    private LinkedHashMap<String, String> hints = new LinkedHashMap<>();
    private List<String> keyOfHints = new ArrayList<>();
    private List<DBDataBean> historyList = new ArrayList<>();
    private Context mContext = this;
    private String tableName = "";
    private DBFinishedInterface finishedInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        Intent intent = getIntent();
        tableName = intent.getStringExtra("TableName");
        Log.e("表名",tableName);
        dbHelper = new DBHelper(this
                ,"DBHelper",null,1);
        finishedInterface = new DBFinishedInterface( ) {
            @Override
            public void onFinish() {
                Log.e("onFinish","DB操作完成回调");
                updateHistory();
            }

            @Override
            public void updateHints(LinkedHashMap<String, String> map) {
                Log.e("updateHints","DB操作完成回调");
                hints.clear();
                hints = map;
                keyOfHints = new ArrayList<>(hints.keySet());
                Log.e("list",keyOfHints.size()+"");
                runOnUiThread(new Runnable( ) {
                    @Override
                    public void run() {
                        if(hintListAdapter!=null){
                            hintListAdapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        };
        dbHelper.setDbFinishedInterface(finishedInterface);
        //初始化控件
        clearAllHistoryText = findViewById(R.id.clear_history_text);
        updateHistory();
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
                searchEditTextView.setSelection(searchEditTextView.getText().length());
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
                        updateHistory();
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
        hintListAdapter = new HintListAdapter(this,keyOfHints);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener( ) {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectText = keyOfHints.get(position);
                searchEditTextView.setText(selectText);
                searchEditTextView.setSelection(searchEditTextView.getText().length());
                Toast.makeText(getApplicationContext(),"url:"+hints.get(selectText)
                        ,Toast.LENGTH_SHORT).show();
            }
        });
        listView.setAdapter(hintListAdapter);
        clearAllHistoryText.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                dbHelper.deleteDate("history","tag",new String[]{tableName});
                updateHistory();
                Toast.makeText(getApplicationContext(),"历史记录已清除！",Toast.LENGTH_SHORT).show();
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
                Log.e("Text变化内容",ss);
                keyOfHints.clear();
                if(ss.equals(""))
                    return;
                dbHelper.onUpdateHints(tableName,new String[]{"title","url"},"title",ss);
            }
        });
    }

    public void updateHistory(){
        historyList.clear();
        if(!dbHelper.IsTableExist("history",dbHelper.getWritableDatabase())){
            dbHelper.createTable("create table history(title text,tag varchar(20))");
        }else{
            historyList = dbHelper.getHistoryList("history",new String[]{"title","tag"}
                    ,"tag",tableName);
        }
        if(historyRecAdapter!=null)
            historyRecAdapter.notifyDataSetChanged();
        if(historyList.size()<=0){
            clearAllHistoryText.setVisibility(View.GONE);
        }else{
            clearAllHistoryText.setVisibility(View.VISIBLE);
        }
    }

    public void addOneHistoryRecord(){
        String text = searchEditTextView.getText().toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", text);
        contentValues.put("tag",tableName);
        dbHelper.getWritableDatabase()
                .insert("history",null,contentValues);
        historyList.add(new DBDataBean(text,tableName));
        updateHistory();
        historyRecAdapter.notifyDataSetChanged();
    }

}
