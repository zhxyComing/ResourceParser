package com.app.dixon.resourceparser.func.home.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.manager.theme.BackType;
import com.app.dixon.resourceparser.core.manager.theme.ThemeManager;
import com.app.dixon.resourceparser.core.pub.view.CircleImageView;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.util.DialogUtils;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;

import java.util.List;

public class SelectAdapter {

    private Context mContext;
    private HorizontalListView mListView;
    private List<Item> mList;

    public SelectAdapter(Context context, HorizontalListView listView) {
        this.mContext = context;
        this.mListView = listView;
    }

    public void setList(List<Item> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.addAll(list);
        }
        notifyData();
    }

    private void notifyData() {
        mListView.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_select, null);
            initView(i, itemView);
            mListView.addChild(itemView);
        }
    }

    private void initView(int position, View itemView) {
        TextView title = itemView.findViewById(R.id.tvTitle);
        TextView titleChinese = itemView.findViewById(R.id.tvTitleChinese);
        CircleImageView cover = itemView.findViewById(R.id.civCover);
        CircleImageView warn = itemView.findViewById(R.id.civWarn);
        FrameLayout bg = itemView.findViewById(R.id.flBackground);

        final Item item = mList.get(position);
        title.setText(item.getTitle());
        TypeFaceUtils.yunBook(title);
        titleChinese.setText(item.getTitleChinese());
        cover.setImageResource(item.getCover());
        setBackground(position, bg, item.getBgColor());

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("You find nothing!");
                DialogUtils.showHomeTipDialog(mContext, item.getMsg());
            }
        });
    }

    private void setBackground(int pos, FrameLayout bg, String bgColor) {
        if ((pos & 1) == 0) {
            setBackground(bg, BackType.LEFT, bgColor);
        } else {
            setBackground(bg, BackType.RIGHT, bgColor);
        }
    }

    //将这个drawable设置给View
    public void setBackground(View view, BackType type, String topColor) {
        ThemeManager.normalBackground(type, topColor, null, view);
    }

    public static class Item {
        private String title;
        private String titleChinese;
        private int cover;
        private String bgColor;
        private String msg;

        public Item() {
        }

        public Item(String title, String titleChinese, int cover, String bgColor, String msg) {
            this.title = title;
            this.titleChinese = titleChinese;
            this.cover = cover;
            this.bgColor = bgColor;
            this.msg = msg;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleChinese() {
            return titleChinese;
        }

        public void setTitleChinese(String titleChinese) {
            this.titleChinese = titleChinese;
        }

        public int getCover() {
            return cover;
        }

        public void setCover(int cover) {
            this.cover = cover;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
