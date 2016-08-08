package com.lt.hm.wovideo.adapter.city;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.CityModel;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/5/16
 */
public class CityListAdapter extends BaseMultiItemQuickAdapter<CityModel> {
	public CityListAdapter(List<CityModel> data) {
		super(data);
		addItemType(CityModel.TITLE, R.layout.layout_city_title);
		addItemType(CityModel.CITY_LIST, R.layout.layout_city_title);
	}

	@Override
	protected void convert(BaseViewHolder holder, CityModel cityModel) {
		switch (holder.getItemViewType()) {
			case CityModel.TITLE:
				holder.setText(R.id.city_title, cityModel.getHead());
				break;
			case CityModel.CITY_LIST:
				holder.setText(R.id.city_title, cityModel.getCity().getCity());
				break;
		}
	}
}
