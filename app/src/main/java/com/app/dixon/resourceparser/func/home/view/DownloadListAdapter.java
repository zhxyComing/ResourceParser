package com.app.dixon.resourceparser.func.home.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.model.MovieDownload;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class DownloadListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieDownload> mList;

    public DownloadListAdapter(Context context, List<MovieDownload> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_down_list, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(final ViewHolder vh, final int position) {
        final MovieDownload md = mList.get(position);
        vh.downloadUrl.setText(md.getDownloadUrl());
        vh.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy(md.getDownloadUrl());
                ToastUtils.toast("链接已复制");
            }
        });
        vh.downloadUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copy(md.getDownloadUrl());
                ToastUtils.toast("链接已复制");
            }
        });
    }

    public void notifyData(List<MovieDownload> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.clear();
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    private static final class ViewHolder {

        TextView downloadUrl;
        ImageView copy;

        public ViewHolder(View view) {
            downloadUrl = view.findViewById(R.id.tvDownloadUrl);
            copy = view.findViewById(R.id.ivCopy);
        }
    }

    private void copy(String url) {
        ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(url);
    }
}
