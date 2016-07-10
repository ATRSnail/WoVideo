package com.lt.hm.wovideo.http.parser;

import com.google.gson.Gson;
import com.lt.hm.wovideo.http.ResponseObj;
import com.lt.hm.wovideo.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/12
 */
public class ResponseParser {
    public static void parse(ResponseObj resp,String value,Class bodyClass, Class headClass){
        JSONObject obj = null;
        try {
            obj = new JSONObject(value);
            if (obj.has("head")){
                JSONObject obj_head = obj.getJSONObject("head");
                resp.setHead(new Gson().fromJson(obj_head.toString(),headClass));
            }
            if (obj.has("body")){
                JSONObject obj_body = obj.optJSONObject("body");
                if (!StringUtils.isNullOrEmpty(obj_body) && obj_body.length()>0){
                    resp.setBody(new Gson().fromJson(obj_body.toString(),bodyClass));
                }else{
                    resp.setBody(null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static void loginParse(ResponseObj resp,String value,Class bodyClass, Class headClass){
        JSONObject obj = null;
        try {
            obj = new JSONObject(value);
            if (obj.has("head")){
                JSONObject obj_head = obj.getJSONObject("head");
                resp.setHead(new Gson().fromJson(obj_head.toString(),headClass));
            }
            if (obj.has("body")){
                JSONObject obj_body = obj.optJSONObject("body");
                if (!StringUtils.isNullOrEmpty(obj_body) && obj_body.length()>0){
                    JSONObject obj_model = obj_body.optJSONObject("user");
                    if (!StringUtils.isNullOrEmpty(obj_model) && obj_model.length()>0){
                        resp.setBody(new Gson().fromJson(obj_model.toString(),bodyClass));
                    }else{
                        resp.setBody(null);
                    }
                }else{
                    resp.setBody(null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void bannerParse(ResponseObj resp,String value,Class bodyClass, Class headClass){
        JSONObject obj = null;
        try {
            obj = new JSONObject(value);
            if (obj.has("head")){
                JSONObject obj_head = obj.getJSONObject("head");
                resp.setHead(new Gson().fromJson(obj_head.toString(),headClass));
            }
            if (obj.has("body")){
                JSONObject obj_body = obj.optJSONObject("body");
                if (!StringUtils.isNullOrEmpty(obj_body) && obj_body.length()>0){
                    resp.setBody(new Gson().fromJson(obj_body.toString(),bodyClass));
                }else{
                    resp.setBody(null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
