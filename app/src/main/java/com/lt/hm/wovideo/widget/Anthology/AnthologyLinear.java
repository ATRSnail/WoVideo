package com.lt.hm.wovideo.widget.Anthology;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.adapter.video.AnthologyGvAdapter;
import com.lt.hm.wovideo.model.PlayList;
import com.lt.hm.wovideo.widget.LineGridView;

import java.util.List;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/9/21
 */
public class AnthologyLinear extends LinearLayout {

    private LineGridView gv;
    private AnthologyGvAdapter gvAdapter;
    private List<PlayList.PlaysListBean> list;

    public AnthologyLinear(Context context, List<PlayList.PlaysListBean> list) {
        super(context);
        this.list = list;
        init(context);
    }

    private void init(Context context) {
        if (list == null || list.size() <= 0) return;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.anthology_linear, this, true);
        gv = (LineGridView) findViewById(R.id.gv_anthology);
        gvAdapter = new AnthologyGvAdapter(context, list);
        gv.setAdapter(gvAdapter);

    }
}
