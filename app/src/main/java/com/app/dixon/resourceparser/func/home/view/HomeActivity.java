package com.app.dixon.resourceparser.func.home.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.pub.view.HorizontalListView;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;
import com.app.dixon.resourceparser.func.movie.recommend.view.MovieOutlineActivity;
import com.app.dixon.resourceparser.func.set.EditActivity;
import com.app.dixon.resourceparser.func.torr.view.TorrActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {

    private HorizontalListView mSelectListView;
    private TextView mGoText;
    private LinearLayout mGoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();
    }

    public static void startHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
        }
    }

    private void initView() {
        TypeFaceUtils.yunBook(mGoText);

        SelectAdapter adapter = new SelectAdapter(this, mSelectListView);
        adapter.setList(loadItemList());

        mGoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity();
            }
        });
    }

    private void startTargetActivity() {
        switch (mSelectListView.getCurrentIndex()) {
            case 0:
                MovieOutlineActivity.startMovieOutlineActivity(this);
                overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
                break;
            case 1:
                TorrActivity.startTorrActivity(this);
                overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
                break;
            case 2:
                EditActivity.startEditActivity(this);
                overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
                break;
        }
    }

    private List<SelectAdapter.Item> loadItemList() {
        //修改为文件存储
        SelectAdapter.Item movie = new SelectAdapter.Item("Find Movie", "搜电影", R.drawable.cover_movie, "#FCD62D");
        SelectAdapter.Item magnet = new SelectAdapter.Item("Find Magnet", "搜磁力", R.drawable.cover_magnet, "#CDDC39");
        SelectAdapter.Item set = new SelectAdapter.Item("Find Message", "信息", R.drawable.cover_message, "#4FC3F7");

        List<SelectAdapter.Item> itemList = new ArrayList<>();
        itemList.add(movie);
        itemList.add(magnet);
        itemList.add(set);

        return itemList;
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mSelectListView = findViewById(R.id.hlvSelectList);
        mGoText = findViewById(R.id.tvGo);
        mGoLayout = findViewById(R.id.llGo);
    }
}
