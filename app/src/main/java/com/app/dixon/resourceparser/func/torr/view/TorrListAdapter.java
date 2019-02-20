package com.app.dixon.resourceparser.func.torr.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.torr.control.TorrParseRequest;
import com.app.dixon.resourceparser.model.TorrDetail;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class TorrListAdapter extends BaseAdapter {

    private Context mContext;
    private List<TorrDetail> mList;
    private OnParseDownloadListener mParseListener;
    private boolean canParse = true; //之后用bean替代bool

    public TorrListAdapter(Context context, List<TorrDetail> list, OnParseDownloadListener listener) {
        this.mContext = context;
        this.mList = list;
        this.mParseListener = listener;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_torr_url, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(final ViewHolder vh, int position) {
        final TorrDetail torrDetail = mList.get(position);
        vh.title.setText(torrDetail.getTitle());
        TypeFaceUtils.yunBook(vh.title);
        if (torrDetail.isHot()) {
            vh.hot.setVisibility(View.VISIBLE);
        } else {
            vh.hot.setVisibility(View.GONE);
        }
        vh.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.toast("长按解析该数据源");
            }
        });
        vh.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                parse(torrDetail.getUrl());
                return true;
            }
        });
    }

    private void parse(String address) {
        if (TextUtils.isEmpty(address)) {
            ToastUtils.toast("链接异常 无法解析");
            return;
        }
        if (!canParse) {
            ToastUtils.toast("当前正在解析 请稍后");
            return;
        }
        canParse = false;
        mParseListener.onParseStart();
        ParserManager.queue().add(new TorrParseRequest(address, new TorrParseRequest.Listener() {
            @Override
            public void onSuccess(String url) {
                mParseListener.onParseSuccess(url);
                canParse = true;
            }

            @Override
            public void onFail(String msg) {
                mParseListener.onParseFail(msg);
                canParse = true;
            }
        }));
    }

    public void notifyData(List<TorrDetail> list) {
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
        ImageView hot;
        CardView card;

        public ViewHolder(View view) {
            title = view.findViewById(R.id.tvTitle);
            hot = view.findViewById(R.id.ivHot);
            card = view.findViewById(R.id.cvCard);
        }
    }

    public interface OnParseDownloadListener {
        void onParseStart();

        void onParseSuccess(String result);

        void onParseFail(String err);
    }
}
