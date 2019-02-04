package com.app.dixon.resourceparser.func.special.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.view.AsyncImageView;
import com.app.dixon.resourceparser.model.SpecialOutline;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class SpecialOutlineAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialOutline> mList;

    private static final String HOST = "https://www.menworld.org";

    public SpecialOutlineAdapter(Context context, List<SpecialOutline> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_special_outline, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(final ViewHolder vh, int position) {
        final SpecialOutline special = mList.get(position);
        vh.title.setText(special.getTitle());
        vh.image.setUrl(null);
        vh.image.setUrl(parseUrl(special.getImg()));

        vh.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (special.getUrl().contains("fanhao")) {
                    SpecialDetailActivity.startSpecialDetailActivity(mContext, special.getTitle(), special.getUrl());
                } else {
                    startChrome(special.getUrl());
                }
            }
        });
    }

    //非关键网址 可选跳转浏览器观看
    private void startChrome(final String url) {
        AlertDialog tipDialog = new AlertDialog.Builder(mContext)
                .setTitle("提示")
                .setMessage("非标准网址，暂时无法解析，是否跳转至浏览器阅读？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse(parseUrl(url));
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mContext.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
        tipDialog.show();
    }

    private String parseUrl(String url) {
        if (url.startsWith("http")) {
            return url;
        }
        return HOST + url;
    }

    public void notifyData(List<SpecialOutline> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.clear();
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addData(List<SpecialOutline> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    private static final class ViewHolder {

        TextView title;
        AsyncImageView image;

        public ViewHolder(View view) {
            title = view.findViewById(R.id.tvTitle);
            image = view.findViewById(R.id.ivCover);
        }
    }
}
