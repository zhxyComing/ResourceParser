package com.app.dixon.resourceparser.core.pub.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

/**
 * Created by dixon.xu on 2018/3/12.
 * <p>
 * 只需要设置url 自动异步的imageView
 */

public class AsyncImageView extends android.support.v7.widget.AppCompatImageView {

    private static final String TAG = "AsyncImageViewV2";
    private float mAspectRatio;
    private DisplayImageOptions.Builder mOptionsBuilder = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.ARGB_8888);

    private ImageSize mImageSize;

    public AsyncImageView(Context context) {
        this(context, null);
    }

    public AsyncImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AsyncImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AsyncImageView setUri(Uri uri) {
        return setUrl(uri != null ? uri.toString() : "");
    }

    public AsyncImageView setUrl(String url, ImageLoadingFailedListener failedListener) {
        final String imageUrl = url != null ? url : "";
        ImageLoader.getInstance().displayImage(imageUrl, new ImageViewAware(this), mOptionsBuilder.displayer(new SimpleBitmapDisplayer()).build(), mImageSize, failedListener, mImageLoadingProgressListener);
        return this;
    }

    public AsyncImageView setUrl(String url) {
        final String imageUrl = url != null ? url : "";
        ImageLoader.getInstance().displayImage(imageUrl, new ImageViewAware(this), mOptionsBuilder.displayer(new SimpleBitmapDisplayer()).build(), mImageSize, mImageLoadingListener, mImageLoadingProgressListener);
        return this;
    }

    public AsyncImageView setFilePath(String filePath) {
        return setUrl(ImageDownloader.Scheme.FILE.wrap(filePath));
    }

    public AsyncImageView setAssetsFilePath(String filePath) {
        return setUrl(ImageDownloader.Scheme.ASSETS.wrap(filePath));
    }

    public AsyncImageView setDrawableResId(int drawableResId) {
        return setUrl(ImageDownloader.Scheme.DRAWABLE.wrap(Integer.toString(drawableResId)));
    }

    /**
     * 以下几个设置默认图的方法 以及设置图片大小的方法 属于初始化的方法 需要在setUrl之前调用
     * <p>
     * 如果想更改这些参数，也需重调setUrl
     *
     * @param resId
     * @return
     */


    public AsyncImageView setDefaultDrawable(int resId) {
        mOptionsBuilder.showImageForEmptyUri(resId);
        return this;
    }

    public AsyncImageView setDefaultDrawable(Drawable drawable) {
        mOptionsBuilder.showImageForEmptyUri(drawable);
        return this;
    }

    public AsyncImageView setLoadingDrawable(int resId) {
        mOptionsBuilder.showImageOnLoading(resId);
        return this;
    }

    public AsyncImageView setLoadingDrawable(Drawable drawable) {
        mOptionsBuilder.showImageOnLoading(drawable);
        return this;
    }

    public AsyncImageView setFailedDrawable(int resId) {
        mOptionsBuilder.showImageOnFail(resId);
        return this;
    }

    public AsyncImageView setFailedDrawable(Drawable drawable) {
        mOptionsBuilder.showImageOnFail(drawable);
        return this;
    }

    public AsyncImageView setImageSize(ImageSize imageSize) {
        mImageSize = imageSize;
        return this;
    }


    private static boolean shouldAdjust(int layoutDimension) {
        return layoutDimension == 0 || layoutDimension == ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    private ImageLoadingListener mImageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    };

    public static abstract class ImageLoadingFailedListener implements ImageLoadingListener {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }

    private ImageLoadingProgressListener mImageLoadingProgressListener = new ImageLoadingProgressListener() {
        @Override
        public void onProgressUpdate(String imageUri, View view, int current, int total) {
//            Log.e(TAG, "onProgressUpdate current=" + current + ", total=" + total + ", imageUri=" + imageUri);
        }
    };

}
