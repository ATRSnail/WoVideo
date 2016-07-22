package com.lt.hm.wovideo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lt.hm.wovideo.model.NetUsage;

/**
 * Created by KECB on 7/22/16.
 */

public class NetUsageDatabase {

    private final DataBaseHelper dbHelper;

    public NetUsageDatabase(Context context) {
        super();
        dbHelper = new DataBaseHelper(context);
    }

    /**
     * 增
     *
     * @param data
     */
    public void insert(NetUsage data) {
        String sql = "insert into " + DataBaseHelper.NET_USAGE_TABLE_NAME;

        sql += "(video_id, create_time, bytes, user_id) values('"+data.getVideoId()+"','"+
                data.getCreateTime()+"','"+
                data.getBytes()+"','"+data.getUserId()+"')";

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
     * @param userId
     */
    public void delete(String userId) {
        SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
        String sql = ("delete from " + DataBaseHelper.HISTORY_TABLE_NAME + " where user_id=?");
        sqlite.execSQL(sql, new String[]{userId});
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
     * 查总数
     *
     * @param userId
     * @return
     */
    public long querySum(String userId) {
        long bytes = 0;
        SQLiteDatabase sqlite = dbHelper.getReadableDatabase();
        Cursor cursor = sqlite.rawQuery("select sum(bytes) from "
                + DataBaseHelper.NET_USAGE_TABLE_NAME + " where user_id = '" + userId + "'", null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if (cursor.getString(0) != null) {
                bytes = Long.parseLong(cursor.getString(0));
            }
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }
        sqlite.close();
        return bytes;
    }
}
