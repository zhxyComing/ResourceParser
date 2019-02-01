package com.app.dixon.resourceparser.core.manager;

import android.content.Context;
import android.graphics.Bitmap;

import com.app.dixon.resourceparser.core.util.FilterLruDiskCache;
import com.app.dixon.resourceparser.core.util.FilterLruMemoryCache;
import com.nostra13.universalimageloader.cache.disc.DiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class AsyncImageLoader {

    private static final int MAX_DISK_CACHE = 50 * 1024 * 1024;
    private static final int THREAD_POOL_SIZE = 3;

    public static void init(Context context) {
        configImageLoader(context);
    }

    /**
     * 初始化imageLoader相关config 方便之后AsyncImageView使用
     * <p>
     * ️这段代码之后需要再次验证研究
     */
    private static void configImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .build();
        int cache = (int) (Runtime.getRuntime().maxMemory() * 0.16f);
        if (cache > MAX_DISK_CACHE) {
            cache = MAX_DISK_CACHE;
        }

        File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);

        DiskCache diskCache = null;
        try {
            diskCache = new FilterLruDiskCache(individualCacheDir, createReserveDiskCacheDir(context), new Md5FileNameGenerator(), MAX_DISK_CACHE) {
                @Override
                public boolean filter(String imageUri) {
                    return imageUri != null && imageUri.startsWith("drawable://");
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(THREAD_POOL_SIZE)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheSize(MAX_DISK_CACHE)
                .memoryCacheSize(cache)
                .memoryCache(new FilterLruMemoryCache(cache) {
                    @Override
                    public boolean filter(String key) {
                        return key != null && key.startsWith("drawable://");
                    }
                })
                .diskCache(diskCache)
                .defaultDisplayImageOptions(options);
        ImageLoader.getInstance().init(builder.build());
    }

    private static File createReserveDiskCacheDir(Context context) {
        File cacheDir = StorageUtils.getCacheDirectory(context, false);
        File individualDir = new File(cacheDir, "uil-images");
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return cacheDir;
    }
}
