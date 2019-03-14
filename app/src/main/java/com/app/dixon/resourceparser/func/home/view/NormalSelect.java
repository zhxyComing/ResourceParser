package com.app.dixon.resourceparser.func.home.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.view.CircleImageView;
import com.app.dixon.resourceparser.core.util.DialogUtils;
import com.app.dixon.resourceparser.core.util.TypeFaceUtils;

/**
 * Created by dixon.xu on 2019/3/13.
 * <p>
 * 普通Home页Item
 */

public class NormalSelect extends SelectAdapter.SelectItem {

    private FrameLayout mBackground;
    private TextView mGoText;
    private LinearLayout mGoLayout;

    public NormalSelect(Context context, SelectAdapter.ItemModel model) {
        super(context, model);
    }

    @Override
    protected View initView() {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_home_select, null);

        TextView title = itemView.findViewById(R.id.tvTitle);
        TextView titleChinese = itemView.findViewById(R.id.tvTitleChinese);
        CircleImageView cover = itemView.findViewById(R.id.civCover);
        CircleImageView warn = itemView.findViewById(R.id.civWarn);
        mBackground = itemView.findViewById(R.id.flBackground);
        mGoText = itemView.findViewById(R.id.tvGo);
        mGoLayout = itemView.findViewById(R.id.llGo);

        title.setText(mModel.getTitle());
        TypeFaceUtils.yunBook(title, mGoText);
        titleChinese.setText(mModel.getTitleChinese());
        cover.setImageResource(mModel.getCover());

        warn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showHomeTipDialog(mContext, mModel.getMsg());
            }
        });

        mGoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity();
            }
        });
        return itemView;
    }

    @Override
    public View getBackgroundView() {
        return mBackground;
    }

    @Override
    public <T> void onEvent(T event) {

    }

    private void startTargetActivity() {
        String openPageClazz = mModel.getOpenPage();
        if (TextUtils.isEmpty(openPageClazz)) {
            return;
        }
        try {
            Class<?> aClass = mContext.getClassLoader().loadClass(openPageClazz);
            mContext.startActivity(new Intent(mContext, aClass));
            if (mContext instanceof Activity) {
                ((Activity) mContext).overridePendingTransition(R.anim.activity_common_in, R.anim.activity_common_out);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
