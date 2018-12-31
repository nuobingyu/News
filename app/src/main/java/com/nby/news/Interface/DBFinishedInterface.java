package com.nby.news.Interface;

import java.util.LinkedHashMap;

public interface DBFinishedInterface {
    public void onFinish();
    public void updateHints(LinkedHashMap<String,String> map);
}