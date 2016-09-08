package com.lt.hm.wovideo.adapter.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.model.LiveModel;
import com.lt.hm.wovideo.model.LiveModles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hxg on 16/8/3.
 */
public class VideoPipAdapter extends BaseAdapter {
    private Context mContext;
    private List<LiveModles> list;
    private final static int TYPE_COUNT = 2;
    private final static int TYPE_CONTENT = 1;
    private final static int TYPE_HEAD = 0;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;     // 初始化isSelected的数据


    private int checkNum;

    public VideoPipAdapter(Context contenxt, List<LiveModles> livemodles) {
        this.mContext = contenxt;
        this.list = livemodles;
        initDate();
    }

    private void initDate() {
        int size = getCount();
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < size; i++) {
            getIsSelected().put(i, false);
        }

    }

    @Override
    public int getCount() {
        int count = 0;

        if (null != list) {
            //  所有分类中item的总和是ListVIew  Item的总个数
            for (LiveModles category : list) {
                count += category.getItemCount();
            }
        }

        return count;
    }

    @Override
    public Object getItem(int position) {
        // 异常情况处理
        if (null == list || position < 0 || position > getCount()) {
            return null;
        }

        // 同一分类内，第一个元素的索引值
        int categroyFirstIndex = 0;

        for (LiveModles category : list) {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            // item在当前分类内
            if (categoryIndex < size) {
                return category.getItem(categoryIndex);
            }

            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            categroyFirstIndex += size;
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // 异常情况处理
        if (null == list || position < 0 || position > getCount()) {
            return TYPE_CONTENT;
        }


        int categroyFirstIndex = 0;

        for (LiveModles category : list) {
            int size = category.getItemCount();
            // 在当前分类中的索引值
            int categoryIndex = position - categroyFirstIndex;
            if (categoryIndex == 0) {
                return TYPE_HEAD;
            }

            categroyFirstIndex += size;
        }

        return TYPE_CONTENT;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        ViewHolde viewhold;
        ViewHolde1 viewhold1;

        switch (type) {
            case TYPE_CONTENT:
                if (null == convertView) {
                    viewhold = new ViewHolde();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.vido_pip__item, parent, false);
                    viewhold.checkbox = (CheckBox) convertView.findViewById(R.id.video_pip_ck);
                    viewhold.tv_content = (TextView) convertView.findViewById(R.id.video_pip_content);
                    convertView.setTag(viewhold);
                } else {
                    viewhold = (ViewHolde) convertView.getTag();
                }
                LiveModel modles = (LiveModel) getItem(position);
                viewhold.tv_content.setText(modles.getTvName());
                viewhold.checkbox.setClickable(false);
//                    viewhold.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            Toast.makeText(mContext,position+"posiont",Toast.LENGTH_SHORT).show();
//                            if (isSelected.get(position)) {
//                                isSelected.put(position, false);
//                                setIsSelected(isSelected);
//                            } else {
//                                isSelected.put(position, true);
//                                setIsSelected(isSelected);
//                            }
//                        }
//                    });
                // 根据isSelected来设置checkbox的选中状况
                viewhold.checkbox.setChecked(getIsSelected().get(position));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkNum <= 4) {
                            if (isSelected.get(position)) {
                                isSelected.put(position, false);
                                setIsSelected(isSelected);
                                viewhold.checkbox.setChecked(getIsSelected().get(position));
                                checkNum--;
                            } else if (checkNum != 4) {
                                isSelected.put(position, true);
                                setIsSelected(isSelected);
                                viewhold.checkbox.setChecked(getIsSelected().get(position));
                                checkNum++;

                            } else if (checkNum == 4) {
                                Toast.makeText(mContext, "最能只能选择四个", Toast.LENGTH_SHORT).show();

                            }

                        }
                    }
                });
                break;
            case TYPE_HEAD:
                if (null == convertView) {
                    viewhold1 = new ViewHolde1();
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.vido_pip__head_item, parent, false);
                    viewhold1.tv_head = (TextView) convertView.findViewById(R.id.video_pip_head);
                    convertView.setTag(viewhold1);
                } else {
                    viewhold1 = (ViewHolde1) convertView.getTag();
                }
                String modles1 = (String) getItem(position);
                if (null != modles1)
                    viewhold1.tv_head.setText(modles1);

                break;
        }

        return convertView;
    }

    public ArrayList<String> getUrls() {
        ArrayList<String> urls = new ArrayList<>();
        Iterator iterator = isSelected.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            int position = (int) entry.getKey();
            boolean flag = (boolean) entry.getValue();
            if (flag) {
                LiveModel liveModel = (LiveModel) getItem(position);
                if (!urls.contains(liveModel.getUrl()))
                    urls.add(liveModel.getUrl());
            }
        }
        return urls;
    }

    public final class ViewHolde {
        public CheckBox checkbox;
        public TextView tv_content;

    }

    public final class ViewHolde1 {
        public TextView tv_head;

    }

    public interface ItemClickCallBack {
        void pipBtnCallBack(ArrayList<String> str);
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        VideoPipAdapter.isSelected = isSelected;
    }
}
