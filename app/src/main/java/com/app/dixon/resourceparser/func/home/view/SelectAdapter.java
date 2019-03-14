package com.app.dixon.resourceparser.func.home.view;

import android.content.Context;
import android.view.View;

import com.app.dixon.resourceparser.core.manager.theme.BackType;
import com.app.dixon.resourceparser.core.manager.theme.ThemeManager;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class SelectAdapter {

    private Context mContext;
    private HorizontalListView mListView;
    private List<ItemModel> mList;
    private List<SelectItem> mCache;

    public SelectAdapter(Context context, HorizontalListView listView) {
        this.mContext = context;
        this.mListView = listView;
        this.mCache = new ArrayList<>();
    }

    public void setList(List<ItemModel> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.addAll(list);
        }
        notifyData();
    }

    private void notifyData() {
        mCache.clear();
        mListView.removeAllViews();
        for (int i = 0; i < mList.size(); i++) {
            ItemModel model = mList.get(i);
            SelectItem item;
            switch (model.getType()) {
                case MUSIC:
                    item = new MusicSelect(mContext, model);
                    break;
                case NORMAL:
                    item = new NormalSelect(mContext, model);
                    break;
                default://异常情况 数据错误
                    item = new NormalSelect(mContext, model);
                    break;
            }
            mListView.addChild(item.getView());
            //统一设置背景
            ThemeManager.normalBackground((i & 1) == 0 ? BackType.LEFT : BackType.RIGHT,
                    model.getBgColor(),
                    null,
                    item.getBackgroundView());
            mCache.add(item);
        }
    }

    public List<ItemModel> getList() {
        return mList;
    }

    //向每个Item发送事件
    public <T> void onEvent(T event) {
        for (SelectItem item : mCache) {
            item.onEvent(event);
        }
    }

    //Home页的一个Item需要的基本参数 额外参数可以继承实现
    public static class ItemModel {

        public enum Type {
            NORMAL("normal"), MUSIC("music");

            private String type;

            Type(String type) {
                this.type = type;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        private String title;
        private String titleChinese;
        private int cover;
        private String bgColor;
        private String msg;
        private Type type;
        private String openPage;

        public ItemModel() {
        }

        public ItemModel(Type type, String title, String titleChinese, int cover, String bgColor, String msg, String openPage) {
            this.type = type;
            this.title = title;
            this.titleChinese = titleChinese;
            this.cover = cover;
            this.bgColor = bgColor;
            this.msg = msg;
            this.openPage = openPage;
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

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public String getOpenPage() {
            return openPage;
        }

        public void setOpenPage(String openPage) {
            this.openPage = openPage;
        }
    }

    //Home页的一个Item
    public static abstract class SelectItem {

        protected Context mContext;
        protected ItemModel mModel;

        public SelectItem(Context context, ItemModel model) {
            mContext = context;
            this.mModel = model;
        }

        public View getView() {
            return initView();
        }

        protected abstract View initView();

        public abstract View getBackgroundView();

        public abstract <T> void onEvent(T event);
    }
}
