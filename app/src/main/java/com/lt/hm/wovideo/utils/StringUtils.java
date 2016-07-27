package com.lt.hm.wovideo.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.format.DateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * 字符串操作工具包
 *
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	private final static Pattern IMG_URL = Pattern
			.compile(".*?(gif|jpeg|png|jpg|bmp)");

	private final static Pattern URL = Pattern
			.compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		return toDate(sdate, dateFormater.get());
	}

	public static Date toDate(String sdate, SimpleDateFormat dateFormater) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String getDateString(Date date) {
		return dateFormater.get().format(date);
	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = null;

		if (TimeZoneUtil.isInEasternEightZones())
			time = toDate(sdate);
		else
			time = TimeZoneUtil.transformTime(toDate(sdate),
					TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());

		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天 ";
		} else if (days > 2 && days < 31) {
			ftime = days + "天前";
		} else if (days >= 31 && days <= 2 * 31) {
			ftime = "一个月前";
		} else if (days > 2 * 31 && days <= 3 * 31) {
			ftime = "2个月前";
		} else if (days > 3 * 31 && days <= 4 * 31) {
			ftime = "3个月前";
		} else {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	public static String friendly_time2(String sdate) {
		String res = "";
		if (isEmpty(sdate))
			return "";

		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		String currentData = StringUtils.getDataTime("MM-dd");
		int currentDay = toInt(currentData.substring(3));
		int currentMoth = toInt(currentData.substring(0, 2));

		int sMoth = toInt(sdate.substring(5, 7));
		int sDay = toInt(sdate.substring(8, 10));
		int sYear = toInt(sdate.substring(0, 4));
		Date dt = new Date(sYear, sMoth - 1, sDay - 1);

		if (sDay == currentDay && sMoth == currentMoth) {
			res = "今天 / " + weekDays[getWeekOfDate(new Date())];
		} else if (sDay == currentDay + 1 && sMoth == currentMoth) {
			res = "昨天 / " + weekDays[(getWeekOfDate(new Date()) + 6) % 7];
		} else {
			if (sMoth < 10) {
				res = "0";
			}
			res += sMoth + "/";
			if (sDay < 10) {
				res += "0";
			}
			res += sDay + " / " + weekDays[getWeekOfDate(dt)];
		}

		return res;
	}


	/**
	 * 智能格式化
	 */
	public static String friendly_time3(String sdate) {
		String res = "";
		if (isEmpty(sdate))
			return "";

		Date date = StringUtils.toDate(sdate);
		if (date == null)
			return sdate;

		SimpleDateFormat format = dateFormater2.get();

		if (isToday(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "上午 hh:mm" : "下午 hh:mm");
			res = format.format(date);
		} else if (isYesterday(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "昨天 上午 hh:mm" : "昨天 下午 hh:mm");
			res = format.format(date);
		} else if (isCurrentYear(date.getTime())) {
			format.applyPattern(isMorning(date.getTime()) ? "MM-dd 上午 hh:mm" : "MM-dd 下午 hh:mm");
			res = format.format(date);
		} else {
			format.applyPattern(isMorning(date.getTime()) ? "yyyy-MM-dd 上午 hh:mm" : "yyyy-MM-dd 下午 hh:mm");
			res = format.format(date);
		}
		return res;
	}

	/**
	 * @return 判断一个时间是不是上午
	 */
	public static boolean isMorning(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int hour = time.hour;
		return (hour >= 0) && (hour < 12);
	}

	/**
	 * @return 判断一个时间是不是今天
	 */
	public static boolean isToday(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year)
				&& (thenMonth == time.month)
				&& (thenMonthDay == time.monthDay);
	}

	/**
	 * @return 判断一个时间是不是昨天
	 */
	public static boolean isYesterday(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;
		int thenMonth = time.month;
		int thenMonthDay = time.monthDay;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year)
				&& (thenMonth == time.month)
				&& (time.monthDay - thenMonthDay == 1);
	}

	/**
	 * @return 判断一个时间是不是今年
	 */
	public static boolean isCurrentYear(long when) {
		android.text.format.Time time = new android.text.format.Time();
		time.set(when);

		int thenYear = time.year;

		time.set(System.currentTimeMillis());
		return (thenYear == time.year);
	}

	/**
	 * 获取当前日期是星期几<br>
	 *
	 * @param dt
	 * @return 当前日期是星期几
	 */
	public static int getWeekOfDate(Date dt) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return w;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 *
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 返回long类型的今天的日期
	 *
	 * @return
	 */
	public static long getToday() {
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater2.get().format(cal.getTime());
		curDate = curDate.replace("-", "");
		return Long.parseLong(curDate);
	}

	public static String getCurTimeDay(String str){
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format1= new SimpleDateFormat("MM-dd");
		try {
			Date date = format.parse(str);
			String  time_result= format1.format(date);
			return time_result;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getCurGroupDay(String str){
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date  date = format.parse(str);
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(date);
			int cal  = calendar.get(Calendar.MONTH);
			int year = calendar.get(Calendar.YEAR);
			return year+"年"+(cal+1)+"月";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String args[]){
		String str = "2016-04-10 13:59:10";
		String weekDay= getCurWeekDay(str);
		System.out.print(weekDay);
	}

	/**
	 * 获取 指定日期为周几
	 * @author leonardo
	 * @param str
	 * @return
	 */

	// TODO: 16/4/11  存在bug ，若 为 去年的 同一天 显示时间 有可能有误
	public static String getCurWeekDay(String str){
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date  date = format.parse(str);
			Calendar calendar= Calendar.getInstance();
			calendar.setTime(date);
			Date date1 = new Date();
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(date1);
			int j=calendar1.get(Calendar.DAY_OF_WEEK);
			int d_year1 = calendar1.get(Calendar.DAY_OF_YEAR);
			int d_year = calendar.get(Calendar.DAY_OF_YEAR);

			int  i  = calendar.get(Calendar.DAY_OF_WEEK);
			if (d_year==d_year1){
				return "今天";
			}
			if ((i-1) == 0) {
				return "周日";
			}
			if ((i-1)== 1) {
				return "周一";
			}
			if ((i-1) == 2) {
				return "周二";
			}
			if ((i-1) == 3) {
				return "周三";
			}
			if ((i-1) == 4) {
				return "周四";
			}
			if ((i-1) == 5) {
				return "周五";
			}
			if ((i-1) == 6) {
				return "周六";
			}

//			switch (i-1){
//				case 1:
//					return "周一";
//				case 2:
//					return "周二";
//				case 3:
//					return "周三";
//				case 4:
//					return "周四";
//				case 5:
//					return "周五";
//				case 6:
//					return "周六";
//				case 7:
//					return "周日";
//				default:break;
//			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getCurTimeStr() {
		Calendar cal = Calendar.getInstance();
		String curDate = dateFormater.get().format(cal.getTime());
		return curDate;
	}

	/***
	 * 计算两个时间差，返回的是的秒s
	 *
	 * @param dete1
	 * @param date2
	 * @return
	 * @author 火蚁 2015-2-9 下午4:50:06
	 */
	public static long calDateDifferent(String dete1, String date2) {

		long diff = 0;

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = dateFormater.get().parse(dete1);
			d2 = dateFormater.get().parse(date2);

			// 毫秒ms
			diff = d2.getTime() - d1.getTime();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return diff / 1000;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 *
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 判断一个url是否为图片url
	 *
	 * @param url
	 * @return
	 */
	public static boolean isImgUrl(String url) {
		if (url == null || url.trim().length() == 0)
			return false;
		return IMG_URL.matcher(url).matches();
	}

	/**
	 * 判断是否为一个合法的url地址
	 *
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str) {
		if (str == null || str.trim().length() == 0)
			return false;
		return URL.matcher(str).matches();
	}

	/**
	 * 字符串转整数
	 *
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception ignored) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 *
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 *
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception ignored) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 *
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception ignored) {
		}
		return false;
	}

	public static String getString(String s) {
		return s == null ? "" : s;
	}

	/**
	 * 将一个InputStream流转换成字符串
	 *
	 * @param is
	 * @return
	 */
	public static String toConvertString(InputStream is) {
		StringBuffer res = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader read = new BufferedReader(isr);
		try {
			String line;
			line = read.readLine();
			while (line != null) {
				res.append(line + "<br>");
				line = read.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != isr) {
					isr.close();
					isr.close();
				}
				if (null != read) {
					read.close();
					read = null;
				}
				if (null != is) {
					is.close();
					is = null;
				}
			} catch (IOException ignored) {
			}
		}
		return res.toString();
	}

	/***
	 * 截取字符串
	 *
	 * @param start 从那里开始，0算起
	 * @param num   截取多少个
	 * @param str   截取的字符串
	 * @return
	 */
	public static String getSubString(int start, int num, String str) {
		if (str == null) {
			return "";
		}
		int leng = str.length();
		if (start < 0) {
			start = 0;
		}
		if (start > leng) {
			start = leng;
		}
		if (num < 0) {
			num = 1;
		}
		int end = start + num;
		if (end > leng) {
			end = leng;
		}
		return str.substring(start, end);
	}

	/**
	 * 获取当前时间为每年第几周
	 *
	 * @return
	 */
	public static int getWeekOfYear() {
		return getWeekOfYear(new Date());
	}

	/**
	 * 获取当前时间为每年第几周
	 *
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		int week = c.get(Calendar.WEEK_OF_YEAR) - 1;
		week = week == 0 ? 52 : week;
		return week > 0 ? week : 1;
	}

	public static int[] getCurrentDate() {
		int[] dateBundle = new int[3];
		String[] temp = getDataTime("yyyy-MM-dd").split("-");

		for (int i = 0; i < 3; i++) {
			try {
				dateBundle[i] = Integer.parseInt(temp[i]);
			} catch (Exception e) {
				dateBundle[i] = 0;
			}
		}
		return dateBundle;
	}

	/**
	 * 返回当前系统时间
	 */
	public static String getDataTime(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}


	/**
	 * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
	 *
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null)
			return true;

		if (obj instanceof CharSequence)
			return ((CharSequence) obj).length() == 0;

		if (obj instanceof Collection)
			return ((Collection) obj).isEmpty();

		if (obj instanceof Map)
			return ((Map) obj).isEmpty();

		if (obj instanceof Object[]) {
			Object[] object = (Object[]) obj;
			if (object.length == 0) {
				return true;
			}
			boolean empty = true;
			for (Object anObject : object) {
				if (!isNullOrEmpty(anObject)) {
					empty = false;
					break;
				}
			}
			return empty;
		}
		return false;
	}

	/**
	 * 下拉刷新标签设定
	 *
	 * @param context
	 * @return
	 */
	public static String getCurrentTime(Context context) {
		String label = DateUtils.formatDateTime(
				context,
				System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		return label;
	}

	/**
	 * 获取一个月之后的时间
	 * @param context
	 * @return
     */
	public static String ToMonthDay(Context context) throws ParseException {
		String cur= getCurrentTime(context);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(sdf.parse(cur));
//		cal.add(java.util.Calendar.DATE, -7); // 向前一周；如果需要向后一周，用正数即可
    	cal.add(java.util.Calendar.MONTH, -1); // 向前一月；如果需要向后一月，用正数即可
		return sdf.format(cal.getTime());
	}

	/**
	 * 读取本地文件中JSON字符串
	 *
	 * @param fileName
	 * @return
	 */
	public static String getJson(String fileName, Context context) {


		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					context.getAssets().open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	/**
	 * 获取时间戳（HH表示 为24小时制，hh 表示 12小时制）
	 *
	 * @return
	 */
	public static String getTimeStamp() {
		Date date = new Date();
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
		return timestamp;
	}

	public static int getFlagTime(String billTime) {
		int result = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date bill_day = format.parse(billTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bill_day);
			Date today = new Date();
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(today);

			if ((today.getTime() - bill_day.getTime()) > 0 && (today.getTime() - bill_day.getTime()) <= 86400000) {
				result = 0;
			} else if ((today.getTime() - bill_day.getTime()) <= 0) {
				result = -1;
			} else if ((today.getTime() - bill_day.getTime()) > 0 && (today.getTime() - bill_day.getTime()) <= 172800000) {
				result = 1;
			} else {
				result = 2;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	// TODO: 16/4/1  测试分组展示
	public static String getFlags(String billTime)
	{
		int result = 0;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date bill_day = format.parse(billTime);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(bill_day);
			Date today = new Date();
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(today);

			if (calendar.getTime().getMonth() == calendar1.getTime().getMonth() && calendar.getTime().getYear()== calendar1.getTime().getYear()){
				return "本月";
			}

			if ((today.getTime() - bill_day.getTime()) > 0 && (today.getTime() - bill_day.getTime()) <= 86400000) {
				result = 0;
			} else if ((today.getTime() - bill_day.getTime()) <= 0) {
				result = -1;
			} else if ((today.getTime() - bill_day.getTime()) > 0 && (today.getTime() - bill_day.getTime()) <= 172800000) {
				result = 1;
			} else {
				result = 2;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}




	private int isYeaterday(Date oldTime, Date newTime) throws ParseException {
		if (newTime == null) {
			newTime = new Date();
		}
		//将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String todayStr = format.format(newTime);
		Date today = format.parse(todayStr);
		//昨天 86400000=24*60*60*1000 一天
		if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
			return 0;
		} else if ((today.getTime() - oldTime.getTime()) <= 0) { //至少是今天
			return -1;
		} else { //至少是前天
			return 1;
		}

	}

	/**
	 * 拼接 数据
	 * @param maps
	 * @return
	 */
	public static String parseHashMap(LinkedHashMap<String,Object> maps){
		StringBuffer result = new StringBuffer();
		Set<String> keys =maps.keySet();
		for (String key :
				keys) {
			Object value =maps.get(key);
			result.append(key).append(":").append(value!=null? value:"").append(";");
		}
		TLog.log(result.toString());
		return result.toString();
	}


	/**
	 * 是否为联通手机号
	 *
	 * @param context
	 * @return
	 */
	public static boolean getOperators(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		boolean flag=false;
		String IMSI = tm.getSubscriberId();
		if (IMSI == null || IMSI.equals("")) {
			return false;
		}
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
//			operator = "中国移动";
			flag=false;
		} else if (IMSI.startsWith("46001")) {
//			operator = "中国联通";
			flag=true;
		} else if (IMSI.startsWith("46003")) {
//			operator = "中国电信";
			flag=false;
		}
		return flag;
	}



	public static String generateStringPosition(long time){
		long hours = (time % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (time % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (time % (1000 * 60)) / 1000;
		if (hours==0&& minutes!=0){
			return minutes + " 分钟 "
					+ seconds + " 秒 ";
		}
		return hours + " 小时 " + minutes + " 分钟 "
				+ seconds + " 秒 ";
	}

	/**
	 * 生成游客唯一ID
	 * @return
     */
	public static String generateOnlyID(){
		Long h = System.currentTimeMillis();//获得当前时间的毫秒数
		String str = h.toString().substring(0,11);//转化为字符串
		int i = str.length();//总长度
		int j = i-7;//用来取此字符串的末尾7位数，因为前面的数是年份什么的基本不变，我们只用后面的7位
		char[] charArray = str.substring(j,i).toCharArray();//将取到的7位数做成数组
		//将26位字母做成数组
		String [] arr = {"a", "b", "c", "d","e", "f", "g",
				"h","i","g", "k", "l","m", "n",
				"o", "p","q", "r", "s", "t",
				"u", "v", "w", "x","y", "z"};
		//将字母数组随机取3个字母组成一个字符串，一共组成3个字符串放到目标数组target中
		StringBuffer uniqueId = new StringBuffer();//用于生成唯一ID
		Random random = new Random();//用于取随机数和布尔值
		boolean insertflag = true;//用来控制是插入数字还是字母
		int timecount = 0;//用来控制插入数字的长度，别超过7
		int zimucount = 0;//用来控制插入字母的总数，别超过9 7个数字加上9个字母组合
		boolean timeflag = true;//判断时间是否插入了7位，默认true为不满
		boolean zimuflag = true;//判断字幕是否插入了9位，默认true为不满
		while (zimucount<7||timecount<4) {//开始组合
			if(insertflag){//默认为ture，先加字母，你也可以先加数字
				if (zimucount<7) {//如果uniqueId插入的字幕总数没超过9个
					uniqueId.append(arr[random.nextInt(26)]);//则在字母数组中随机选一个插入
					zimucount++;//对应加1
					if(timeflag){//如果时间没有插入满7位则重新抓阄看插入时间还是数字
						insertflag = random.nextBoolean();//重置flag，随机产生false还是true
					}//如果timeflag=false,时间数字已经插入满7位，则不抓阄了。保持insertflag=true
				}else{//如果已经加够了否则不操作，
					zimuflag = false;//将zimuflag变为已加够，false
					insertflag = false;//将插入权判断给时间数字
				}
			}else{
				if (timecount<4) {//先加时间转化成的数组，你也可以先加字母
					//此处取时间数字数组不能用random随机取。那样用时间来生成数组就没意义了
					uniqueId.append(charArray[timecount]);//不可打乱顺序
					timecount++;//对应加1
					if(zimuflag){//如果字母没有插入满9位则重新抓阄看插入时间还是数字
						insertflag = random.nextBoolean();
					}//如果zimuflag=false,字幕已经插入满9位，则不抓阄了。保持insertflag=false
				}else{
					timeflag = false;//将timeflag变为已加够，false
					insertflag = true;//将插入权判断给字母
				}
			}
		}
		return  uniqueId.toString();
	}


}
