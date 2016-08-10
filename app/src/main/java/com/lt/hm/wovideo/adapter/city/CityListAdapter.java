package com.lt.hm.wovideo.adapter.city;

import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.City;
import com.lt.hm.wovideo.model.CityModel;
import com.lt.hm.wovideo.widget.MySectionIndexer;

import java.util.List;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 8/5/16
 */
public class CityListAdapter extends BaseQuickAdapter<City> {

	private MySectionIndexer<City> indexer;

	public CityListAdapter(int layoutResId, List<City> data,MySectionIndexer<City> indexer) {
		super(layoutResId, data);
		this.indexer = indexer;
	}

	@Override
	protected void convert(BaseViewHolder holder, City city) {
     holder.setText(R.id.text_place,city.getCity());
		int section = indexer.getSectionForPosition(holder.getLayoutPosition());
		if (holder.getLayoutPosition() == indexer.getPositionForSection(section)) {
			holder.setText(R.id.sort_key,Character.toUpperCase(city.pyOne.charAt(0)) + "");
			holder.getView(R.id.sort_key).setVisibility(View.VISIBLE);
		} else {
			holder.getView(R.id.sort_key).setVisibility(View.GONE);
		}

	}
}
