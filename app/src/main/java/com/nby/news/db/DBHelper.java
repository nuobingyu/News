package com.nby.news.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;


import com.nby.news.Interface.DBFinishedInterface;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper{
    private static final int mCapacity = 12;
    private static DBFinishedInterface dbFinishedInterface;

    public void setDbFinishedInterface(DBFinishedInterface dbFinishedInterface) {
        this.dbFinishedInterface = dbFinishedInterface;
    }

    public void removeDbFinishedInterface(){
        if(this.dbFinishedInterface!=null)
            dbFinishedInterface = null;
    }

    private LinkedHashMap<String,String> dataMap = new LinkedHashMap<String,String>(mCapacity,0.75f,true){
        @Override
        protected boolean removeEldestEntry(Entry eldest) {
            return size()>mCapacity;
        }
    };
    private List<DBDataBean> historyList = new ArrayList<>();

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
        onFinish();
    }

    public void onFinish(){
        if(dbFinishedInterface!=null){
            Log.e("onFinish","sql操作完成回调");
            dbFinishedInterface.onFinish();
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

    public void deleteDate(String tableName,String columnName,Object[] params){
        SQLiteDatabase database;
        String sql = "delete from "+tableName+" where "+columnName+"=?";
        database = this.getWritableDatabase();
        database.execSQL(sql,params);
        onFinish();
    }

    public void updateDate(String tableName,String columnName,Object[] params){
        SQLiteDatabase database;
        String sql = "update "+tableName+" set "+columnName+"=? where "+columnName+"=?";
        database = this.getWritableDatabase();
        database.execSQL(sql);
        onFinish();
    }

    public void queryHistoryTable(String tableName ,String[] columnNames,String keyName,String key){
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(tableName
                ,columnNames
                ,keyName+" LIKE"+" '%"+key+"%'"
                ,null
                ,null
                ,null
                ,null);
            historyList.clear();
            for(int i =0 ; i< mCapacity&& cursor.moveToNext(); i++){
                historyList.add(new DBDataBean(cursor.getString(0),cursor.getString(1)));
                Log.e("query_his",""+cursor.getString(0));
            }
    }

    @SuppressLint("Recycle")
    public void queryXTable(String tableName,String[] columnNames,String keyName,String s){
        SQLiteDatabase database;
        database = this.getReadableDatabase();
        Cursor cursor = database.query(tableName
                ,columnNames
                ,keyName+" LIKE"+" '%"+s+"%'"
                ,null
                ,null
                ,null
                ,null);

        if(cursor.getColumnCount()==2){
            for(int i =0 ; i< mCapacity&& cursor.moveToNext(); i++){
                dataMap.put(cursor.getString(0),cursor.getString(1));
//                Log.e("query",""+cursor.getString(0)
//                        +","+cursor.getString(1));
            }
        }
        Log.e("query",""+dataMap.size());
        if(dbFinishedInterface!=null) {
            dbFinishedInterface.updateHints(dataMap);
        }
    }

    public void updateHints(){
        if(dbFinishedInterface!=null)
            dbFinishedInterface.updateHints(dataMap);
    }

    public void onUpdateHints(String tableName,String[] columnNames,String keyName,String s){
        if(dataMap.size()<=0){
            queryXTable(tableName,columnNames,keyName,s);
        }
        updateHints();
    }

    public List<DBDataBean> getHistoryList(String tableName,String[] columnNames,String keyName,String s){
        if(historyList.size()<=0){
//            Log.e("getHistoryList"," "+historyList.size());
            queryHistoryTable(tableName,columnNames,keyName,s);
        }
        return historyList;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
