package com.app.dixon.resourceparser.func.home.control;

import android.content.Context;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.dson.Dson;
import com.app.dixon.resourceparser.core.dson.DsonData;
import com.app.dixon.resourceparser.core.util.FileUtils;
import com.app.dixon.resourceparser.func.home.view.SelectAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/2/28.
 * <p>
 * 首页数据加载解析
 */

public class HomeItemLoader {

    public static List<SelectAdapter.Item> loadItem(Context context) {
        List<SelectAdapter.Item> itemList = new ArrayList<>();
        String fromAssets = FileUtils.getFromAssets("home.dson", context);
        try {
            List<DsonData> selects = Dson.parse(fromAssets);
            for (int i = 0; i < selects.size(); i++) {
                DsonData data = selects.get(i);
                SelectAdapter.Item item = new SelectAdapter.Item(getType(data),
                        data.get("title"),
                        data.get("titleChinese"),
                        0,
                        data.get("bg"),
                        data.get("msg"),
                        data.get("openPage"));
                setItemCover(item, data.get("title"));
                itemList.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itemList;
    }

    private static SelectAdapter.Item.Type getType(DsonData data) {
        SelectAdapter.Item.Type type = null;
        switch (data.get("type")) {
            case "normal":
                type = SelectAdapter.Item.Type.NORMAL;
                break;
            case "music":
                type = SelectAdapter.Item.Type.MUSIC;
                break;
        }
        return type;
    }

    private static void setItemCover(SelectAdapter.Item item, String title) {
        if (title.contains("Movie")) {
            item.setCover(R.drawable.cover_movie);
        } else if (title.contains("Magnet")) {
            item.setCover(R.drawable.cover_magnet);
        } else if (title.contains("Message")) {
            item.setCover(R.drawable.cover_message);
        } else if (title.contains("Music")) {
            item.setCover(R.drawable.cover_music);
        }
    }

}
