package com.lt.hm.wovideo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lt.hm.wovideo.R;
import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * @author xch
 * @version 1.0
 * @create_date 16/11/28
 */
public class VideoListAdapter extends BaseAdapter {

    public static final String TAG = "JieCaoVideoPlayer";

    int[] videoIndexs = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    Context context;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return videoIndexs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "why you always getview");

        ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_videoview, null);
            viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.jcVideoPlayer.setUp(
                VideoConstant.videoUrls[position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                VideoConstant.videoTitles[position]);

        Picasso.with(convertView.getContext())
                .load(VideoConstant.videoThumbs[position])
                .into(viewHolder.jcVideoPlayer.thumbImageView);
        return convertView;
    }

    class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
