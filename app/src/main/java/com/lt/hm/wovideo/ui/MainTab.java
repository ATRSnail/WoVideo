package com.lt.hm.wovideo.ui;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.fragment.ClassPage;
import com.lt.hm.wovideo.fragment.EventsPage;
import com.lt.hm.wovideo.fragment.LivePageFragment;
import com.lt.hm.wovideo.fragment.NewChoicePage;
import com.lt.hm.wovideo.fragment.RecommendPage;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/5/30
 */
public enum MainTab {
	//    SELECTION(0, R.string.choice_string, R.drawable.tab_choice_selector,
//            ChoicePage.class),
	SELECTION(0, R.string.new_choice_string, R.drawable.tab_choice_selector,
					NewChoicePage.class),
//	CLASSES(1, R.string.classes_string, R.drawable.tab_classes_selector, ClassPage.class),

	    CLASSES(1, R.string.live_string, R.drawable.tab_selection_selector, LivePageFragment.class),
//    VIP(2, R.string.vip_string, R.drawable.tab_vip_selector, RecommendPage.class),
//    OLYMPIC(2,R.string.olympic_string,R.drawable.tab_olympic_selector,OlympicPage.class),
	VIP(2, R.string.vip_string, R.drawable.tab_vip_selector, RecommendPage.class),
	EVENTS(3, R.string.event_string, R.drawable.tab_events_selector, EventsPage.class);
	private int idx;
	private int resName;
	private int resIcon;
	private Class<?> clz;

	private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
		this.idx = idx;
		this.resName = resName;
		this.resIcon = resIcon;
		this.clz = clz;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getResIcon() {
		return resIcon;
	}

	public void setResIcon(int resIcon) {
		this.resIcon = resIcon;
	}

	public Class<?> getClz() {
		return clz;
	}

	public void setClz(Class<?> clz) {
		this.clz = clz;
	}
}
