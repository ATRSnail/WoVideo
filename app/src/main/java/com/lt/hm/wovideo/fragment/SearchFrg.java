package com.lt.hm.wovideo.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lt.hm.wovideo.R;

import net.robinx.lib.blur.StackBlur;
import net.robinx.lib.blur.utils.BlurUtils;
import net.robinx.lib.blur.widget.BlurDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/14
 */
public class SearchFrg extends Fragment{

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.img_bg)
    ImageView mBlurDrawableRelativeLayout;

    private Bitmap bgBitmap;

    public static SearchFrg newInstance(Bitmap bitmap) {
        SearchFrg fragment = new SearchFrg();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bg",bitmap);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_search,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ButterKnife.bind(this,getActivity());
        tv_name.setText("ssss");
        bgBitmap = getArguments().getParcelable("bg");
        //background
        Bitmap compressedBgBitmap = BlurUtils.compressBitmap(bgBitmap,8);
        Bitmap blurBgBitmap = StackBlur.blurNativelyPixels(compressedBgBitmap,4,false);
        mBlurDrawableRelativeLayout.setImageBitmap(blurBgBitmap);
//        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mBlurDrawableRelativeLayout,View.ALPHA,0,1f);
//        alphaAnimator.setDuration(2000);
//        alphaAnimator.start();
    }
}
