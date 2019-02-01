package com.app.dixon.resourceparser.func.home.model;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.core.pub.view.AsyncImageView;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.home.control.MovieDetailRequest;
import com.app.dixon.resourceparser.model.MovieDetail;
import com.app.dixon.resourceparser.model.MovieOutline;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class MovieOutlineAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieOutline> mList;

    public MovieOutlineAdapter(Context context, List<MovieOutline> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_movie_outline, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(final ViewHolder vh, int position) {
        MovieOutline movie = mList.get(position);
        vh.title.setText(movie.getTitle());
        vh.image.setImageBitmap(null);

        ParserManager.queue().add(new MovieDetailRequest(movie.getTitle(), movie.getUrl(), new MovieDetailRequest.Listener() {
            @Override
            public void onSuccess(MovieDetail detail) {
                vh.image.setUrl(detail.getCoverImg());
                vh.image.setOnLongClickListener(new ClipboardCopyListener(mContext, detail.getDownloadUrl()));
            }
        }));

        vh.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast("长按复制下载链接");
            }
        });
    }

    public void notifyData(List<MovieOutline> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.clear();
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

    private static class ClipboardCopyListener implements View.OnLongClickListener {

        private Context mContext;
        private String mUrl;

        public ClipboardCopyListener(Context context, String url) {
            this.mContext = context;
            this.mUrl = url;
        }

        @Override
        public boolean onLongClick(View v) {
            ToastUtils.toast("链接已复制到剪切板");
            copy();
            return true;
        }

        private void copy() {
            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(mUrl);
        }
    }
}
