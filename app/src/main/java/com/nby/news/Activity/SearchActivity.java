package com.nby.news.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nby.news.Bean.NewsBean;
import com.nby.news.R;
import com.nby.news.unit.FileUnit;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity{

    private GridLayout gridLayout;
    private EditText searchEditTextView;
    private String[] hotTitles = {"测试一","测试一","测试一","测试一","测试一","测试一"};
    private List<NewsBean> beanList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_layout);
        gridLayout = findViewById(R.id.gridLayout_search);
        searchEditTextView = findViewById(R.id.search_inputText1);
        init();
        beanList.addAll(new FileUnit(SearchActivity.this).readFromTempFile());
        searchEditTextView.addTextChangedListener(new TextWatcher( ) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String ss = s.toString();
                Log.e("搜素内容",ss);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void search(View view){
        String s = searchEditTextView.getText().toString();
        for(int i = 0 ; i< beanList.size() ; i++){
            if(!s.equals("")&&s.contains(" ")&&beanList.get(i).title.contains(s)){
                Toast.makeText(this,"存在",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(SearchActivity.this,"没有呐~",Toast.LENGTH_SHORT ).show();
    }

    public void init(){
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        for(int i = 0 ; i < 6; i++){
            TextView tv = new TextView(SearchActivity.this);
            tv.setText(hotTitles[i]);
            gridLayout.addView(tv);
        }
    }
}
