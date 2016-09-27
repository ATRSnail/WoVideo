package com.lt.hm.wovideo.widget.Anthology;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.interf.onFunctionListener;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/26
 */
public class FunctionLinear extends LinearLayout {

    private TextView zanTv;
    private TextView touTv;
    private TextView fenTv;
    private TextView souTv;

    private onFunctionListener listener;

    public FunctionLinear(Context context, onFunctionListener listener) {
        super(context);
        this.listener = listener;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.linear_function, this, true);

        zanTv = (TextView) findViewById(R.id.tv_zan);
        touTv = (TextView) findViewById(R.id.tv_tou);
        fenTv = (TextView) findViewById(R.id.tv_fen);
        souTv = (TextView) findViewById(R.id.tv_shou);

        zanTv.setOnClickListener(v -> {
            if (listener != null)
                listener.onZanClick();
        });
        touTv.setOnClickListener(v -> {
            if (listener != null)
                listener.onTouClick();
        });
        fenTv.setOnClickListener(v -> {
            if (listener != null)
                listener.onShareClick();
        });
        souTv.setOnClickListener(v -> {
            if (listener != null)
                listener.onCollectClick();
        });


    }


}
