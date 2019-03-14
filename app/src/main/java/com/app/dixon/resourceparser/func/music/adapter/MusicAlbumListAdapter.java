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
import com.app.dixon.resourceparser.core.util.Ln;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.music.control.MusicLocalManager;
import com.app.dixon.resourceparser.model.MusicAlbum;
import com.app.dixon.resourceparser.model.MusicInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by dixon.xu on 2019/3/7.
 */

public class MusicAlbumListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MusicAlbum> mList;

    public MusicAlbumListAdapter(Context context, List<MusicAlbum> list) {
        this.mContext = context;
        this.mList = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_music_album_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(ViewHolder vh, int position) {
        MusicAlbum album = mList.get(position);
        vh.num.setText(String.valueOf(position));
        vh.title.setText(album.getName() == null ? String.valueOf(album.getId()) : album.getName());
        vh.count.setText(String.valueOf(album.getInfos().size()));
        if ((position & 1) == 0) {
            vh.itemLayout.setBackgroundColor(Color.parseColor("#F9F9F9"));
        } else {
            vh.itemLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void notifyData(List<MusicAlbum> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.clear();
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    private static final class ViewHolder {

        TextView num;
        TextView title;
        TextView count;
        LinearLayout itemLayout;

        public ViewHolder(View view) {
            num = view.findViewById(R.id.tvNum);
            count = view.findViewById(R.id.tvCount);
            title = view.findViewById(R.id.tvTitle);
            itemLayout = view.findViewById(R.id.llItemLayout);
            TypeFaceUtils.yunBook(num, count, title);
        }
    }

    public List<MusicAlbum> getList() {
        return mList;
    }
}
