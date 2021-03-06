package com.lt.hm.wovideo.utils;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author leonardo
 * @deprecated
 */
public class TimeZoneUtil {

	/**
	 * 判断用户的设备时区是否为东八区（中国）
	 *
	 * @return
	 */
	public static boolean isInEasternEightZones() {
		boolean defaultVaule = true;
		defaultVaule = TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08");
		return defaultVaule;
	}

	/**
	 * 根据不同时区，转换时间
	 *
	 * @param date
	 * @param oldZone
	 * @param newZone
	 * @return
	 */
	public static Date transformTime(Date date, TimeZone oldZone, TimeZone newZone) {
		Date finalDate = null;
		if (date != null) {
			int timeOffset = oldZone.getOffset(date.getTime())
					- newZone.getOffset(date.getTime());
			finalDate = new Date(date.getTime() - timeOffset);
		}
		return finalDate;
	}
}
