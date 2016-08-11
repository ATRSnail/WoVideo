package com.lt.hm.wovideo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/6/6
 */
public class CustomTopbar extends RelativeLayout {
    // 自定义的控件和自定义的属性（values下mytopbar.xml）的声明
    private ImageView leftImage;
    private Button rightImage;
    private TextView tvTitle;

    private Drawable leftDrawable;

    private float titleTextSize;
    private int titleTextColor;
    private String titleText;


    private LayoutParams leftLayoutParams, titleLayoutParams, rightLayoutParams;

    private myTopbarClicklistenter clicklistenter;

    //重写构造方法
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public CustomTopbar(Context context, AttributeSet attrs) {

        super(context, attrs);

        // 获取自定义的属性，并将自定义的属性映射到自定义的属性值中去
        // 通过obtainStyledAttributes获取自定义属性，并存到TypedArray
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.secondTopbar);

        leftDrawable = ta.getDrawable(R.styleable.secondTopbar_leftBackGround);

        titleTextSize = ta.getDimension(R.styleable.secondTopbar_CustomtitleTextSize, 0);
        titleTextColor = ta.getColor(R.styleable.secondTopbar_CustomtitleTextColor, 0);
        titleText = ta.getString(R.styleable.secondTopbar_Customtoptitle);

        ta.recycle();

        //组件定义
        leftImage = new ImageView(context);
        tvTitle = new TextView(context);
        rightImage = new Button(context);

        // 将自定义的属性设置到控件上
        leftImage.setImageDrawable(leftDrawable);

        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(TypedValue.DENSITY_DEFAULT, titleTextSize);
        tvTitle.setText(titleText);
        tvTitle.setGravity(Gravity.CENTER);    // 设置文字居中

//        rightImage.setImageDrawable(rightDrawable);
//        rightImage.setBackground(getResources().getDrawable(rightDrawable));
        rightImage.setBackgroundColor(getResources().getColor(R.color.white));
        rightImage.setTextColor(getResources().getColor(R.color.blue_btn_bg_color));
        setBackgroundColor(getResources().getColor(R.color.white));    // 设置背景颜色

        //将自定义的控件放到Layout中（以LayoutParams的形式）
        leftLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);     //设置左对齐
        leftLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        leftLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
//        leftLayoutParams.setMarginStart((int) getResources().getDimension(R.dimen.activity_horizontal_margin));
        leftImage.setPaddingRelative(20, 0, 15, 0);
        addView(leftImage, leftLayoutParams);  //leftButton以leftLayoutParams的形式加入到ViewGroup中

        titleLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        titleLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);  //设置居中对齐
        addView(tvTitle, titleLayoutParams);    //tvTitle以titleLayoutParams的形式加入到ViewGroup中

        rightLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE); //设置右对齐
        rightLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
//        rightLayoutParams.setMarginEnd((int) getResources().getDimension(R.dimen.activity_horizontal_margin));
        rightImage.setPaddingRelative(15, 0, 15, 0);
        addView(rightImage, rightLayoutParams);//rightButton以rightLayoutParams的形式加入到ViewGroup中

        //设置监听事件
        leftImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicklistenter == null) return;
                clicklistenter.leftClick();
            }
        });

        rightImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clicklistenter == null) return;
                clicklistenter.rightClick();
            }
        });

    }

    //自定义一个setOnClickListenter方法
    public void setOnTopbarClickListenter(myTopbarClicklistenter clicklistenter) {
        this.clicklistenter = clicklistenter;   //调用的时候通过一个匿名内部类映射进来
    }

    //设置左Button是否显示
    public void setLeftIsVisible(boolean flag) {
        if (flag) {
            leftImage.setVisibility(View.VISIBLE);
        } else {
            leftImage.setVisibility(View.GONE);
        }
    }

    public void setRightText(String text){
        if (rightImage!=null){
            rightImage.setText(text);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    // 设置右Button是否显示
    public void setRightIsVisible(boolean flag) {
        if (flag) {
            rightImage.setVisibility(View.VISIBLE);
        } else {
            rightImage.setVisibility(View.GONE);
        }
    }

    public void setTvTitle(String text) {
        tvTitle.setText(text);
    }

    //自定义click的监听回调接口
    public interface myTopbarClicklistenter {
        void leftClick();

        void rightClick();
    }

}