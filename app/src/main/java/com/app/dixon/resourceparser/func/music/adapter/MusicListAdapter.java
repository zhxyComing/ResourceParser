package com.app.dixon.resourceparser.func.music.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.music.control.MusicLocalManager;
import com.app.dixon.resourceparser.model.MusicInfo;

import java.util.List;

/**
 * Created by dixon.xu on 2019/3/7.
 */

public class MusicListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MusicInfo> mList;
    private MusicInfo mPlayingMusic;

    public MusicListAdapter(Context context, List<MusicInfo> list) {
        this.mContext = context;
        this.mList = list;
        this.mPlayingMusic = MusicLocalManager.getPlayingMusic();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_music_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(ViewHolder vh, int position) {
        MusicInfo info = mList.get(position);
        vh.num.setText(String.valueOf(position));
        vh.title.setText(info.getMusicName());
        vh.time.setText(timeParse(info.getDuration()));
        if ((position & 1) == 0) {
            vh.itemLayout.setBackgroundColor(Color.parseColor("#F9F9F9"));
        } else {
            vh.itemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        setPlayingItem(vh, info);
    }

    private void setPlayingItem(ViewHolder vh, MusicInfo itemInfo) {
        if (mPlayingMusic != null && mPlayingMusic.equals(itemInfo)) {
            vh.title.setTextColor(Color.parseColor("#4caf50"));
        } else {
            vh.title.setTextColor(Color.parseColor("#1976d2"));
        }
    }

    public void notifyData(List<MusicInfo> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.clear();
            mList.addAll(list);
        }
        mPlayingMusic = MusicLocalManager.getPlayingMusic();
        notifyDataSetChanged();
    }

    public void notifyData() {
        mPlayingMusic = MusicLocalManager.getPlayingMusic();
        notifyDataSetChanged();
    }


    private static final class ViewHolder {

        TextView num;
        TextView title;
        TextView time;
        LinearLayout itemLayout;

        public ViewHolder(View view) {
            num = view.findViewById(R.id.tvNum);
            time = view.findViewById(R.id.tvTime);
            title = view.findViewById(R.id.tvTitle);
            itemLayout = view.findViewById(R.id.llItemLayout);
            TypeFaceUtils.yunBook(num, time, title);
        }
    }

    public static String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }

    public List<MusicInfo> getList() {
        return mList;
    }
}
