package com.lt.hm.wovideo.ui;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.city.CityListAdapter;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.model.CityModel;
import com.lt.hm.wovideo.utils.PinyinUtil;
import com.lt.hm.wovideo.utils.StringUtils;
import com.lt.hm.wovideo.utils.TLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/5/16
 */
public class CityListPage extends BaseActivity {
	@BindView(R.id.city_list)
	RecyclerView cityList;
	CityListAdapter mAdapter;
	String city_json;
	private String arrays[] = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"
					, "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
	};

	@Override
	protected void init(Bundle savedInstanceState) {
		AssetManager manager = getAssets();
		try {
			InputStream is = manager.open("citys.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuffer stringBuffer = new StringBuffer();
			String str = null;
			while ((str = reader.readLine()) != null) {
				stringBuffer.append(str);
			}
			city_json = stringBuffer.toString();
			TLog.log("json_city" + stringBuffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	@Override
	protected int getLayoutId() {
		return R.layout.layout_city_list;
	}

	@Override
	public void initViews() {
		if (!StringUtils.isNullOrEmpty(city_json)) {
			List<CityModel> models = new ArrayList<>();
			JsonToModel(models, city_json);
			mAdapter = new CityListAdapter(models);
			cityList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
			cityList.setAdapter(mAdapter);
//			mAdapter.notifyDataSetChanged();
		}
	}

	private void JsonToModel(List<CityModel> models, String city_json) {
		try {
			JSONArray array = new JSONArray(city_json);
			if (array != null && array.length() > 0) {
				for (int i = 0; i < arrays.length; i++) {
					for (int j = 0; j < array.length(); j++) {
						CityModel model= new CityModel();
						JSONObject obj = array.getJSONObject(j);
						CityModel.City city_model = new Gson().fromJson(obj.toString(), CityModel.City.class);

						if (i==0){
							model.setItemType(CityModel.TITLE);
							model.setHead(arrays[i]);
							models.add(model);
						}
						if (arrays[i]== PinyinUtil.getPinYinHeadChar(city_model.getCity())){
							model.setItemType(CityModel.CITY_LIST);
							model.setCity(city_model);
							models.add(model);
						}else{
							CityModel model1 = new CityModel();
							model1.setHead(arrays[i]);
							model1.setItemType(CityModel.TITLE);
							models.add(model1);
						}

					}
				}


			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


	}

	@Override
	public void initDatas() {

	}

}
