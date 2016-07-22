package com.lt.hm.wovideo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/19
 */
public class DataBaseHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "wovideo";
    public static final String HISTORY_TABLE_NAME = "video_history";
    public static final String SEARCH_TABLE_NAME = "video_search";
    public static final String NET_USAGE_TABLE_NAME = "net_usage_history";

    public static final String CREATE_HISTORY_TABLE = "create table "
            + HISTORY_TABLE_NAME
            + " (_id integer primary key autoincrement,vid varchar(50),"
            + " create_time varchar(10),img_url varchar(200) ,current_position integer(40), name text)";


    public static final String CREATE_SEARCH_HISTORY_TABLE = "" +
            "create table "+SEARCH_TABLE_NAME+"(_id integer primary key autoincrement,name varchar(200))";

    public static final String CREATE_NET_USAGE_TABLE = "create table "
            + NET_USAGE_TABLE_NAME
            + " (_id integer primary key autoincrement,video_id varchar(50),"
            + " create_time varchar(10), bytes varchar(200), user_id varchar(32))";

    public static final String DELETE_HISTORY_TABLE="create table ";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_HISTORY_TABLE);
        db.execSQL(CREATE_SEARCH_HISTORY_TABLE);
        db.execSQL(CREATE_NET_USAGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_HISTORY_TABLE);
        db.execSQL(CREATE_SEARCH_HISTORY_TABLE);
        db.execSQL(CREATE_NET_USAGE_TABLE);
    }
}
