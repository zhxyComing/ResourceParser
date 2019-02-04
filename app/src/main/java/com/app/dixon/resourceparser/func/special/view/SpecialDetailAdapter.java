package com.app.dixon.resourceparser.func.special.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.view.AsyncImageView;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.model.SpecialDetail;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class SpecialDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialDetail> mList;

    private static final String HOST = "https://www.menworld.org";

    public SpecialDetailAdapter(Context context, List<SpecialDetail> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_special_detail, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(final ViewHolder vh, int position) {
        SpecialDetail special = mList.get(position);
        vh.article.setText(special.getArticle());
        vh.image.setImageDrawable(null);
        vh.image.setUrl(parseUrl(special.getImg()));
        vh.image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtils.toast("当前版本仅提供番号 下载链接请自行前往【Torrentkitty】输入番号获取");
                return true;
            }
        });
    }

    private String parseUrl(String url) {
        if (url.startsWith("http")) {
            return url;
        }
        return HOST + url;
    }

    public void notifyData(List<SpecialDetail> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.clear();
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    private static final class ViewHolder {

        TextView article;
        AsyncImageView image;

        public ViewHolder(View view) {
            article = view.findViewById(R.id.tvArticle);
            image = view.findViewById(R.id.ivCover);
        }
    }
}
