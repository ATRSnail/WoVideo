package com.lt.hm.wovideo.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.CateTagListModel;
import com.lt.hm.wovideo.model.CateTagModel;
import com.lt.hm.wovideo.model.ChannelModel;
import com.lt.hm.wovideo.utils.TLog;

import java.util.LinkedHashMap;
import java.util.List;
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
    Map<String, Map<String, CateTagModel>> movie_container = new LinkedHashMap<>();


    public SelectMenuPop(Context context, String id, CateTagListModel cateTag) {
        super(context);
        updateMenu(context, id, cateTag);
    }

    public void updateMenu(Context context, String id, CateTagListModel cateTag) {
        initMenuHash(id, cateTag);
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
        layout_container.setAlpha(0.8f);
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

    public void showPopupWindow(View parent, boolean shown) {
        if (!shown) {
            // 以下拉方式显示popupwindow
//            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
//            this.showAtLocation(parent, Gravity.LEFT, 0, -90);
            this.showAsDropDown(parent);
            this.setFocusable(true);
            shown = true;
        } else {
            shown = false;
            this.dismiss();
        }
    }

    private void addViews(LinearLayout layout_container, Context context, String key) {
        LinearLayout layout_item = new LinearLayout(context);
        layout_item.setOrientation(LinearLayout.HORIZONTAL);
        layout_item.setPadding(25, 20, 25, 20);
        TextView text = new TextView(context);
        LinearLayout.LayoutParams typelp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        typelp.setMargins(20, 12, 20, 12);
        text.setLayoutParams(typelp);
        text.setText(key);
        text.setPadding(30, 10, 30, 10);
        text.setTextColor(Color.WHITE);
        text.setGravity(Gravity.CENTER);
        text.setBackgroundResource(R.drawable.blue_circle);
        HorizontalScrollView scrollView = new HorizontalScrollView(context);
        scrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout layout_scroll = new LinearLayout(context);
        layout_scroll.setOrientation(LinearLayout.HORIZONTAL);
        RadioGroup group = new RadioGroup(context);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        group.setLayoutParams(params);
        group.setGravity(Gravity.CENTER_VERTICAL);
        group.setOrientation(RadioGroup.HORIZONTAL);
        Map<String, CateTagModel> values = movie_container.get(key);
        Set<String> keys = values.keySet();
        LinearLayout.LayoutParams params1 = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(20, 10, 20, 10);
        params1.gravity = Gravity.CENTER;
        for (String key_name : keys) {
            RadioButton radiobutton = new RadioButton(context);
            radiobutton.setLayoutParams(params1);
            radiobutton.setPadding(20, 0, 20, 0);
            radiobutton.setText(values.get(key_name).getName());
            radiobutton.setTextColor(context.getResources().getColor(R.color.text_press_selector));
            radiobutton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            radiobutton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    radiobutton.setTextColor(context.getResources().getColor(R.color.float_button_color));
                    if (listener != null) {
                        TLog.log(key + "::::" + key_name);
                        listener.clickListener(key, values.get(key_name));
                    }
                } else {
                    radiobutton.setTextColor(context.getResources().getColor(R.color.class_font_color));
                }
            });
            group.addView(radiobutton);
            //防止选中两个
            if (values.get(key_name).equals("全部")) {
                group.check(radiobutton.getId());
            }
        }

        layout_scroll.addView(group);
        scrollView.addView(layout_scroll);
        layout_item.addView(text);
        layout_item.addView(scrollView);
        layout_container.addView(layout_item);
    }


    private void initMenuHash(String id, CateTagListModel cateTag) {
        List<CateTagModel> cateTagModels;
        TLog.error("id---" + id + "-----" + ChannelModel.FILM_ID);

        // 初始化  电影菜单
        Map<String, CateTagModel> map_type = new LinkedHashMap<>();
        Map<String, CateTagModel> map_attr = new LinkedHashMap<>();
        Map<String, CateTagModel> map_area = new LinkedHashMap<>();
        Map<String, CateTagModel> map_time = new LinkedHashMap<>();
        cateTagModels = cateTag.getLx();
        //类型
        if (cateTagModels != null) {
            for (int i = 0; i < cateTagModels.size() + 1; i++) {

                map_type.put(i + "", i == 0 ? new CateTagModel("全部","")  : cateTagModels.get(i - 1));
            }
        }
        //属性
        cateTagModels = cateTag.getSx();
        if (cateTagModels != null) {
            for (int i = 0; i < cateTagModels.size() + 1; i++) {
                map_attr.put(i + "", i == 0 ? new CateTagModel("全部","") : cateTagModels.get(i - 1));
            }
        }
        // 地区
        cateTagModels = cateTag.getDq();
        if (cateTagModels != null) {
            for (int i = 0; i < cateTagModels.size() + 1; i++) {
                map_area.put(i + "", i == 0 ? new CateTagModel("全部","")  : cateTagModels.get(i - 1));
            }
        }
        cateTagModels = cateTag.getNd();
        if (cateTagModels != null) {
            for (int i = 0; i < cateTagModels.size() + 1; i++) {
                map_time.put(i + "", i == 0 ? new CateTagModel("全部","")  : cateTagModels.get(i - 1));
            }
        }
        TLog.error("type---" + map_type.toString());
        movie_container.put(type_string, map_type);
        movie_container.put(attr_string, map_attr);
        movie_container.put(area_string, map_area);
        movie_container.put(time_string, map_time);

    }

    public interface OnRadioClickListener {
        void clickListener(String key, CateTagModel value);
    }

}
