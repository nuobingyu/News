package com.nby.news.Activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.nby.news.R;
import com.nby.news.unit.SharedPreferencesUnit;

public class SearchActivity extends Activity{

    private GridLayout gridLayout_hotNews;
    private GridLayout gridLayout_history;
    private EditText searchEditTextView;
    private String[] hotSearchStrings = {"测试一","测试二","测试三","测试四","测试五","测试六"};
    private String[] historySearchStrings = {"a","b","c","d","e","f","g"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        //初始化控件
        searchEditTextView = findViewById(R.id.search_activity_input);
        gridLayout_hotNews = findViewById(R.id.gridLayout_hotSearch);
        gridLayout_history = findViewById(R.id.gridLayout_history);
        searchEditTextView = findViewById(R.id.search_activity_input);
        //获取搜索数据源
        SharedPreferencesUnit spUnit = new SharedPreferencesUnit(this);
        spUnit.getNewsBean("News");
        //设置监听
        searchEditTextView.setOnKeyListener(new View.OnKeyListener( ) {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP){
                    Log.e("监听到enter"," ");
                    Intent intent = new Intent(Intent.ACTION_SEARCH);
                    intent.putExtra(SearchManager.QUERY,searchEditTextView.getText());
                    startActivity(intent);
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
                String ss = s.toString();
                Log.e("Text变化内容",ss);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent){
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }
    }

    public void doMySearch(String s){
        //这里进行搜索操作
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
