package com.lt.hm.wovideo.sendemail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by songchenyu on 16/7/19.
 */

public class ZhengZeTools {
    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\\\.][A-Za-z]{2,3}([\\\\.][A-Za-z]{2})?$";

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile(REGEX_MOBILE);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEmail(String strEmail) {
        Pattern p = Pattern.compile(REGEX_EMAIL);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
    public static boolean isQQ(String strEmail) {
        String strPattern = "^[1-9][0-9]{5,9}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
}
