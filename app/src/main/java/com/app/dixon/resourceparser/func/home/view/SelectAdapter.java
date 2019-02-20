package com.app.dixon.resourceparser.func.home.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.view.BackgroundDrawable;
import com.app.dixon.resourceparser.core.pub.view.CircleImageView;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
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

        Item item = mList.get(position);
        title.setText(item.getTitle());
        TypeFaceUtils.yunBook(title);
        titleChinese.setText(item.getTitleChinese());
        cover.setImageResource(item.getCover());
        setBackground(position, bg, item.getBgColor());

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.toast("You find nothing!");
            }
        });
    }

    private void setBackground(int pos, FrameLayout bg, String bgColor) {
        if ((pos & 1) == 0) {
            setBackground(bg, 90, 75, bgColor);
        } else {
            setBackground(bg, 75, 90, bgColor);
        }
    }

    //将这个drawable设置给View
    public void setBackground(View view, int left, int right, String topColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            BackgroundDrawable drawable = BackgroundDrawable.builder()
                    .left(left)//设置左侧斜切点的高度（取值范围是大于0，小于100）
                    .right(right)
                    .topColor(Color.parseColor(topColor))//设置上半部分的颜色（默认是白色）
//                    .bottomColor(Color.parseColor("#76C4EB"))//（默认是白色）
                    .build();

            view.setBackground(drawable);
        }
    }

    public static class Item {
        private String title;
        private String titleChinese;
        private int cover;
        private String bgColor;

        public Item() {
        }

        public Item(String title, String titleChinese, int cover, String bgColor) {
            this.title = title;
            this.titleChinese = titleChinese;
            this.cover = cover;
            this.bgColor = bgColor;
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
    }
}
