package com.lt.hm.wovideo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lt.hm.wovideo.model.VideoHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/19
 * 观看历史记录
 */
public class HistoryDataBase {
    private final DataBaseHelper dbHelper;

    public HistoryDataBase(Context context) {
        super();
        dbHelper = new DataBaseHelper(context);
    }

    /**
     * 增
     *
     * @param data
     */
    public void insert(VideoHistory data) {
        String sql = "insert into " + DataBaseHelper.HISTORY_TABLE_NAME;

        sql += "(vid, create_time, img_url, current_position, name) values('"+data.getmId()+"','"+
                data.getCreate_time()+"','"+
                data.getImg_url()+"',"+data.getCurrent_positon()+",'"+data.getmName()+"')";

        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
//        sqlite.execSQL(sql, new String[]{data.getmId() + "",
//                data.getCreate_time() + "", data.getImg_url() + "", data.getCurrent_positon(),
//                data.getmName() + ""});
        sqlite.execSQL(sql);
        sqlite.close();
    }

    /**
     * 删
     *
     * @param id
     */
    public void delete(String id) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DataBaseHelper.HISTORY_TABLE_NAME + " where vid=?");
        sqlite.execSQL(sql, new String[]{id});
        sqlite.close();
    }

    /**
     * 删除多个
     *
     * @param ids
     */
    public void deleteList(List<String> ids) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DataBaseHelper.HISTORY_TABLE_NAME + " where vid=?");
        for (int i = 0; i < ids.size(); i++) {
            sqlite.execSQL(sql, new String[]{ids.get(i)});
        }
        sqlite.close();
    }
//

    /**
     * 改
     *
     * @param data
     */
    public void update(VideoHistory data) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
//    (vid, create_time, img_url, current_position, name)
        String sql = ("update " + DataBaseHelper.HISTORY_TABLE_NAME + " set vid=?, create_time=?, img_url=?, current_position=?, name=? where vid=?");
        sqlite.execSQL(sql,
                new String[]{data.getmId() + "", data.getCreate_time() + "", data.getImg_url() + "",
                        data.getCurrent_positon() +
                                "", data.getmName() + "",
                        data.getmId() + ""});
        sqlite.close();
    }


    /**
     * 查询列表
     *@param uid
     * @return
     */
//    public List<VideoHistory> query(String uid) {
//        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
//        ArrayList<VideoHistory> data = null;
//        data = new ArrayList<VideoHistory>();
//        Cursor cursor = sqlite.rawQuery("select * from "
//                + DataBaseHelper.HISTORY_TABLE_NAME + "where uid= " + uid, null);
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//            VideoHistory history = new VideoHistory();
//            history.setmId(cursor.getInt(1));
//            history.setCreate_time(cursor.getString(2));
//            history.setImg_url(cursor.getString(3));
//            history.setCurrent_positon(cursor.getString(4));
//            history.setmName(cursor.getString(5));
//            data.add(history);
//        }
//        if (!cursor.isClosed()) {
//            cursor.close();
//        }
//        sqlite.close();
//        return data;
//    }

    /**
     * 查
     *
     * @param where
     * @return
     */
    public List<VideoHistory> query(String where) {
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        ArrayList<VideoHistory> data = null;
        data = new ArrayList<VideoHistory>();
        Cursor cursor = sqlite.rawQuery("select * from "
                + DataBaseHelper.HISTORY_TABLE_NAME + where, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            VideoHistory history = new VideoHistory();
            history.setmId(cursor.getString(1));
            history.setCreate_time(cursor.getString(2));
            history.setImg_url(cursor.getString(3));
            history.setCurrent_positon(cursor.getLong(4));
            history.setmName(cursor.getString(5));
            data.add(history);
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
    public void save(VideoHistory data) {
        List<VideoHistory> datas = query(" where vid= '" + data.getmId()+"'");
        if (datas != null && !datas.isEmpty()) {
            update(data);
        } else {
            insert(data);
        }
    }

}
