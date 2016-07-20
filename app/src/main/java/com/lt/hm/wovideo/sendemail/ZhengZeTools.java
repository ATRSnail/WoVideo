package com.lt.hm.wovideo.sendemail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by songchenyu on 16/7/19.
 */

public class ZhengZeTools {


    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    public static boolean isEmail(String strEmail) {
        Pattern p = Pattern.compile("([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,5})+");
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
