package com.app.dixon.resourceparser.core.manager.theme;

import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.app.dixon.resourceparser.core.pub.view.BackgroundDrawable;

/**
 * Created by dixon.xu on 2019/2/22.
 * <p>
 * 主题后续开发 临时方案
 */

public class ThemeManager {

    public static void normalBackground(BackType type, @Nullable String topColor, @Nullable String bottomColor, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            BackgroundDrawable.IShapeDrawableBuilder builder = BackgroundDrawable.builder()
                    //设置左侧斜切点的高度（取值范围是大于0，小于100）
                    .left(type.getLeft())
                    .right(type.getRight());
            if (!TextUtils.isEmpty(topColor)) {
                builder.topColor(Color.parseColor(topColor));//设置上半部分的颜色（默认是白色）
            }
            if (!TextUtils.isEmpty(bottomColor)) {
                builder.bottomColor(Color.parseColor(bottomColor));//设置上半部分的颜色（默认是白色）
            }
            view.setBackground(builder.build());
        }
    }

}
