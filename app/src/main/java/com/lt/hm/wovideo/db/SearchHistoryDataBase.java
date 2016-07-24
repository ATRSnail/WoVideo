package com.lt.hm.wovideo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/19
 */
public class SearchHistoryDataBase {
    private final DataBaseHelper dbHelper;

    public SearchHistoryDataBase(Context context) {
        super();
        dbHelper = new DataBaseHelper(context);
    }


    /**
     * 增
     *
     * @param data
     */
    public void insert(String data) {
        String sql = "insert into " + DataBaseHelper.SEARCH_TABLE_NAME +"(name) values('"+data+"')";

        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        sqlite.execSQL(sql);
        sqlite.close();
    }

    /**
     * 删
     *
     * @param name
     */
    public void delete(String name) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DataBaseHelper.SEARCH_TABLE_NAME + " where name='"+name+"'");
        sqlite.execSQL(sql);
        sqlite.close();
    }



    /**
     * 删除多个
     *
     * @param names
     */
    public void deleteList(List<String> names) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DataBaseHelper.SEARCH_TABLE_NAME + " where name=?");
        for (int i = 0; i < names.size(); i++) {
            sqlite.execSQL(sql, new String[]{names.get(i) + ""});
        }
        sqlite.close();
    }
//

    /**
     * 改
     *
     * @param data
     */
    public void update(String data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
//    (vid, create_time, img_url, current_position, name)
        String sql = ("update " + DataBaseHelper.SEARCH_TABLE_NAME + " set name='"+data+"' where name='"+data+"'");
        sqlite.execSQL(sql);
        sqlite.close();
    }


    /**
     * 查
     *
     * @return
     */
    public List<String> query(String where) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<String> data = null;
        data = new ArrayList<String>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DataBaseHelper.SEARCH_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            String result = new String();
            result= cursor.getString(1);
            data.add(result);
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return data;
    }

    /**
     * 保存一条数据到本地(若已存在则直接覆盖)
     *
     * @param data
     */
    public void save(String data) {
        List<String> datas = query(" where name='" + data+"'");
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }

}
