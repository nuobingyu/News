package com.nby.news.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper{
    private Map<String,String> dataMap = new HashMap<>();
    private List<String> historyList = new ArrayList<>();

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql = "create table search(title varchar(40))";
//        db.execSQL(sql);
//        sql = "create table history(record varchar(40))";
//        db.execSQL(sql);
    }

    public void createTable(String sql){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(sql);
    }


    public void deleteTable(String tableName){
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        if(IsTableExist(tableName,database)){
            String sql = "drop table "+tableName;
            database.execSQL(sql);
        }
    }

    @SuppressLint("Recycle")
    public boolean IsTableExist(String tabName, SQLiteDatabase db) {
        boolean result = false;
        if (tabName == null) {
            return false;
        }
        Cursor cursor = null;
        String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
                + tabName.trim() + "' ";
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getInt(0);
            if (count > 0) {
                result = true;
            }
        }
        return result;
    }


    //"insert into search (title) values(?)"
    public void addOneDate(String sql,Object[] params){
        SQLiteDatabase database;
        database = this.getWritableDatabase();
        database.execSQL(sql,params);
    }

    public void deleteDate(String tableName,String columnName,Object[] params){
        SQLiteDatabase database;
        String sql = "delete from "+tableName+" where "+columnName+"=?";
        database = this.getReadableDatabase();
        database.execSQL(sql,params);
    }

    public void updateDate(String tableName,String columnName,Object[] params){
        SQLiteDatabase database;
        String sql = "update "+tableName+" set "+columnName+"=? where "+columnName+"=?";
        database = this.getWritableDatabase();
        database.execSQL(sql);
    }

    @SuppressLint("Recycle")
    public void queryDate(String tableName,String[] columnNames,String keyName,String s){
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(tableName
                ,columnNames
                ,keyName+" LIKE"+" '%"+s+"%'"
                ,null
                ,null
                ,null
                ,null);

        if(cursor.getColumnCount()==1){
            historyList.clear();
            for(int i =0 ; i< 20&& cursor.moveToNext(); i++){
               historyList.add(cursor.getString(0));
               Log.e("query",""+cursor.getString(0));
            }
        }
        if(cursor.getColumnCount()==2){
            for(int i =0 ; i< 20&& cursor.moveToNext(); i++){
                dataMap.put(cursor.getString(0),cursor.getString(1));
                Log.e("query",""+cursor.getString(0)
                        +","+cursor.getString(1));
            }
        }
    }

    public Map<String,String> getDataMap(String tableName,String[] columnNames,String keyName,String s){
        if(dataMap.size()<=0){
            queryDate(tableName,columnNames,keyName,s);
        }
        return dataMap;
    }

    public List<String> getHistoryList(String tableName,String[] columnNames,String keyName,String s){
        if(historyList.size()<=0){
            queryDate(tableName,columnNames,keyName,s);
        }
        return historyList;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
