package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.VideoType;
import com.lt.hm.wovideo.utils.TLog;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/2
 */
public class SelectMenuPop extends PopupWindow {
    OnRadioClickListener listener;
    String type_string = "类型";
    String attr_string = "属性";
    String area_string = "地区";
    String time_string = "年代";
    Map<String, Map<String, String>> movie_container = new LinkedHashMap<>();
    String[] movie_type_names = new String[]{
            "全部", "剧情", "喜剧", "动作", "恐怖", "动画", "武侠", "警匪", "战争", "爱情",
            "科幻", "奇幻", "犯罪", "冒险", "灾难", "伦理", "传记", "家庭", "记录", "惊悚", "历史",
            "悬疑"
    };
    String[] movie_attr_names = new String[]{"全部", "院线", "预告", "大电影"};
    String[] teleplay_type_names = new String[]{
            "全部", "古装", "武侠", "警匪", "战争", "神话", "科幻", "悬疑", "历史", "爱情", "喜剧", "都市", "农村", "奇幻", "其他"
    };
    String[] teleplay_attr_names = new String[]{
            "全部", "高清全集", "自制剧", "精选", "TVB", "韩剧", "美剧", "英剧", "台剧"
    };
    String[] varity_type_names = new String[]{
            "全部", "情感", "访谈", "真人秀", "选秀", "生活", "时尚", "美食", "纪实", "文化", "曲艺", "演唱会"
    };
    String[] varity_attr_names = new String[]{
            "全部", "正片", "短片"
    };
    String[] sport_type_names = new String[]{
            "全部", "NBA", "CBA", "中国女篮", "英超", "西甲	", "意甲", "德甲", "欧冠", "中超", "国际足球", "亚冠", "综合", "体育", "网球", "世界杯", "欧洲杯", "奥运会", "竞	速", "格斗", "电子竞技", "排球", "极限", "其他"
    };
    String[] area_names = new String[]{
            "全部", "内地", "港台", "美国", "韩国", "日本", "英国", "法国", "俄罗斯", "新加坡", "加拿大", "泰国", "印	度", "西班牙", "马来西亚"
    };
    String[] time_names = new String[]{
            "全部", "2016", "2015", "2013", "2012", "2010", "00年代", "90年代"
    };
    public SelectMenuPop(Context context, int id) {
        super(context);
        updateMenu(context,id);
    }

    public void updateMenu(Context context,int id){
        initMenuHash(id);
        initViews(context);
    }


    public void setListener(OnRadioClickListener listener) {
        this.listener = listener;
    }

    private void initViews(Context context) {
        // TODO: 16/7/2 填充菜单数据并显示 并添加部分内容回调 方便控制 搜索结果显示
//        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view= inflater.inflate()

        LinearLayout layout_container = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout_container.setOrientation(LinearLayout.VERTICAL);
        layout_container.setBackgroundColor(context.getResources().getColor(R.color.white));

        layout_container.setLayoutParams(params);
        Set<String> keys = movie_container.keySet();
        for (String key : keys) {
            addViews(layout_container, context, key);
        }
        this.setContentView(layout_container);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(true);

    }

    public void showPopupWindow(View parent,boolean shown) {
        if (!shown) {
            // 以下拉方式显示popupwindow
//            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
//            this.showAtLocation(parent, Gravity.LEFT, 0, -90);
            this.showAsDropDown(parent);
            this.setFocusable(true);
            shown=true;
        } else {
            shown=false;
            this.dismiss();
        }
    }

    private void addViews(LinearLayout layout_container, Context context, String key) {
        LinearLayout layout_item = new LinearLayout(context);
        layout_item.setOrientation(LinearLayout.HORIZONTAL);
        layout_item.setPadding(15,15,15,15);
        layout_item.setBackgroundColor(Color.parseColor("#30000000"));
        TextView text = new TextView(context);
        text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        text.setText(key);
        text.setGravity(Gravity.CENTER_VERTICAL);
        text.setTextColor(context.getResources().getColor(R.color.black));
        HorizontalScrollView scrollView = new HorizontalScrollView(context);
        scrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout layout_scroll = new LinearLayout(context);
        layout_scroll.setOrientation(LinearLayout.HORIZONTAL);
        RadioGroup group = new RadioGroup(context);
        RadioGroup.LayoutParams params= new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        group.setLayoutParams(params);
        group.setOrientation(RadioGroup.HORIZONTAL);
        Map<String, String> values = movie_container.get(key);
        Set<String> keys = values.keySet();
        for (String key_name :
                keys) {
//            RadioButton radiobutton = new RadioButton(context,null,R.style.radioButton_Style);
            RadioButton radiobutton = new RadioButton(context);
            radiobutton.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.anthology_selector));
            LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.leftMargin=20;
            params1.gravity=Gravity.CENTER;
            radiobutton.setLayoutParams(params1);
            radiobutton.setPadding(20,0,20,0);
            radiobutton.setText(values.get(key_name));
            radiobutton.setTextColor(context.getResources().getColor(R.color.black));
            radiobutton.setButtonDrawable(null);
            radiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (listener != null) {
                            TLog.log(key + "::::" + key_name);
                            listener.clickListener(key, key_name);
                        }
                    }
                }
            });
            group.addView(radiobutton);
            //防止选中两个
            if (radiobutton.getText().toString().equals("全部")) {
                group.check(radiobutton.getId());
            }
        }

        layout_scroll.addView(group);
        scrollView.addView(layout_scroll);
        layout_item.addView(text);
        layout_item.addView(scrollView);
        layout_container.addView(layout_item);
    }


    private void initMenuHash(int id) {
        if (id == VideoType.MOVIE.getId()) {
            // 初始化  电影菜单
            Map<String, String> map_type = new LinkedHashMap<>();
            Map<String, String> map_attr = new LinkedHashMap<>();
            //类型
            for (int i = 0; i < movie_type_names.length; i++) {
                map_type.put(i + "", movie_type_names[i]);
            }
            //属性
            for (int i = 0; i < movie_attr_names.length; i++) {
                map_attr.put(i + "", movie_attr_names[i]);
            }
            AddMenus(movie_container, map_type, map_attr);
        } else if (id == VideoType.TELEPLAY.getId()) {
            //初始化 电视剧菜单
            Map<String, String> map_type = new LinkedHashMap<>();
            Map<String, String> map_attr = new LinkedHashMap<>();
            //类型
            for (int i = 0; i < teleplay_type_names.length; i++) {
                map_type.put(i + "", teleplay_type_names[i]);
            }
            //属性
            for (int i = 0; i < teleplay_attr_names.length; i++) {
                map_attr.put(i + "", teleplay_attr_names[i]);
            }
            AddMenus(movie_container, map_type, map_attr);

        } else if (id == VideoType.VARIATY.getId()) {
            //综艺
            Map<String, String> map_type = new LinkedHashMap<>();
            Map<String, String> map_attr = new LinkedHashMap<>();

            //类型
            for (int i = 0; i < varity_type_names.length; i++) {
                map_type.put(i + "", varity_type_names[i]);
            }
            //属性
            for (int i = 0; i < varity_attr_names.length; i++) {
                map_attr.put(i + "", varity_attr_names[i]);
            }
            AddMenus(movie_container, map_type, map_attr);
        } else {
            //电影
            Map<String, String> map_type = new LinkedHashMap<>();

            //类型
            for (int i = 0; i < sport_type_names.length; i++) {
                map_type.put(i + "", sport_type_names[i]);
            }
            movie_container.put(type_string, map_type);
        }
    }


    private void AddMenus(Map<String, Map<String, String>> movie_container, Map<String, String> map_type, Map<String, String> map_attr) {
        Map<String, String> map_area = new LinkedHashMap<>();
        Map<String, String> map_time = new LinkedHashMap<>();
        // 地区
        for (int i = 0; i < area_names.length; i++) {
            map_area.put(i + "", area_names[i]);
        }
        for (int i = 0; i < time_names.length; i++) {
            map_time.put(i + "", time_names[i]);
        }
        movie_container.put(type_string, map_type);
        movie_container.put(attr_string, map_attr);
        movie_container.put(area_string, map_area);
        movie_container.put(time_string, map_time);
    }

    public interface OnRadioClickListener {
        void clickListener(String key, String value);
    }

}
