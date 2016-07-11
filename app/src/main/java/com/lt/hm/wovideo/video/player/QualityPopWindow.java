package com.lt.hm.wovideo.video.player;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.video.model.VideoModel;
import com.lt.hm.wovideo.video.model.VideoUrl;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/11
 */
public class QualityPopWindow extends PopupWindow {
    private Context mContext;
    private VideoModel mModel;
    OnQulitySelect listener;
    public QualityPopWindow(Context context, VideoModel model) {
        super(context);
        this.mContext= context;
        this.mModel= model;
        initViews();
    }

    public void setListener(OnQulitySelect listener) {
        this.listener = listener;
    }

    private void initViews() {
        LinearLayout layout_container = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_container.setOrientation(LinearLayout.VERTICAL);
        layout_container.setBackgroundColor(mContext.getResources().getColor(R.color.font_black));
        layout_container.setLayoutParams(params);
        RadioGroup group = new RadioGroup(mContext);
        if (mModel==null){
            return;
        }
        for (int i = 0; i < mModel.getmVideoUrl().size(); i++) {
            VideoUrl videoUrl= mModel.getmVideoUrl().get(i);
            RadioButton button = new RadioButton(mContext);
//            button.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.anthology_selector));
            button.setBackgroundColor(mContext.getResources().getColor(R.color.black));
            button.setTextColor(mContext.getResources().getColor(R.color.white));
            LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params1.leftMargin=20;
            params1.gravity= Gravity.RIGHT;
            button.setLayoutParams(params1);
            button.setPadding(20,0,20,0);
            button.setText(videoUrl.getFormatName());
            button.setButtonDrawable(null);
            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (listener != null) {
                            QualityPopWindow.this.dismiss();
                            listener.selected(videoUrl.getFormatName(), videoUrl.getFormatUrl());
                        }
                    }
                }
            });
            group.addView(button);
        }
        layout_container.addView(group);

        this.setContentView(layout_container);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setOutsideTouchable(true);

    }

    public void showPopupWindow(View parent, boolean shown) {
        if (!shown) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
//            this.showAtLocation(parent, Gravity.LEFT, 0, -90);
//            this.showAsDropDown(parent);
            this.setFocusable(true);
            shown=true;
        } else {
            shown=false;
            this.dismiss();
        }
    }

    public  interface  OnQulitySelect{
        void selected(String key,String value);
    }

}
