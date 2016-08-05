package com.lt.hm.wovideo.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.TLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/5/16
 */
public class CityListPage extends BaseActivity {
	@BindView(R.id.city_list)
	RecyclerView cityList;

	@Override
	protected int getLayoutId() {
		return R.layout.layout_city_list;
	}

	@Override
	protected void init(Bundle savedInstanceState) {
		AssetManager manager= getAssets();
		try {
			InputStream  is= manager.open("citys.json");
			BufferedReader reader= new BufferedReader(new InputStreamReader(is));
			StringBuffer stringBuffer=  new StringBuffer();
			String str = null;
			while ((str= reader.readLine())!=null){
				stringBuffer.append(str);
			}
			TLog.log("json_city"+stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initViews() {

	}

	@Override
	public void initDatas() {

	}

}
